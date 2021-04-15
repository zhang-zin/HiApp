package com.zj.hi_debugtools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.zj.hi_library.util.HiDisplayUtil
import java.lang.reflect.Method

class DebugToolDialog : AppCompatDialogFragment() {

    private val debugTools = arrayOf(DebugTools::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val parent = dialog?.window?.findViewById(android.R.id.content) ?: container
        val view = inflater.inflate(R.layout.debug_tool_dialog_layout, parent, false)

        dialog?.window?.apply {
            setLayout(
                (HiDisplayUtil.getDisplayWidthInPx(view.context) * 0.7f).toInt(),
                WindowManager.LayoutParams.WRAP_CONTENT
            )

            setBackgroundDrawableResource(R.drawable.shape_hi_debug_tool)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val rvDebugTool = view.findViewById<RecyclerView>(R.id.rv_debug_tool)
        val decoration = DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL)
        decoration.setDrawable(
            ContextCompat.getDrawable(
                view.context,
                R.drawable.shape_hi_debug_tool_divider
            )!!
        )
        rvDebugTool.addItemDecoration(decoration)

        val debugFunctions = mutableListOf<DebugFunction>()
        for (debugTool in debugTools) {
            val target = debugTool.getConstructor().newInstance()
            val methods = target.javaClass.declaredMethods
            for (method in methods) {
                var title = ""
                var desc = ""
                var enable = false
                val annotation = method.getAnnotation(HiDebug::class.java)
                var parameterType = 0
                if (annotation == null) {
                    method.isAccessible = true
                    title = method.invoke(target) as String
                } else {
                    title = annotation.name
                    desc = annotation.desc
                    enable = true
                    parameterType = annotation.parameterType
                }
                debugFunctions.add(
                    DebugFunction(
                        title,
                        desc,
                        method,
                        target,
                        enable,
                        parameterType
                    )
                )
            }
        }

        rvDebugTool.adapter = DebugToolsAdapter(debugFunctions)
    }

    inner class DebugToolsAdapter(private val debugFunctions: MutableList<DebugFunction>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflate = LayoutInflater.from(parent.context)
                .inflate(R.layout.debug_tool_item_layout, parent, false)
            return object : RecyclerView.ViewHolder(inflate) {}
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val debugFunction = debugFunctions[position]
            val title = holder.itemView.findViewById<TextView>(R.id.tv_tool_title)
            val desc = holder.itemView.findViewById<TextView>(R.id.tv_tool_desc)
            title.text = debugFunction.toolName
            if (debugFunction.toolDesc.isEmpty()) {
                desc.visibility = View.GONE
            } else {
                desc.visibility = View.VISIBLE
                desc.text = debugFunction.toolDesc
            }
            if (debugFunction.enable) {
                title.setOnClickListener {
                    dismiss()
                    if (debugFunction.parameterType > 0) {
                        if (debugFunction.parameterType == 1) {
                            debugFunction.method.invoke(debugFunction.target, activity)
                        }
                    } else {
                        debugFunction.method.invoke(debugFunction.target)
                    }
                }
            }
        }

        override fun getItemCount() = debugFunctions.size
    }

    data class DebugFunction(
        val toolName: String,
        val toolDesc: String,
        val method: Method,
        val target: Any,
        val enable: Boolean,
        val parameterType: Int = 0
    )
}