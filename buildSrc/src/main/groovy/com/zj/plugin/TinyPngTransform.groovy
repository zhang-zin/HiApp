package com.zj.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import javassist.ClassPool
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

class TinyPngTransform extends Transform {

    private ClassPool classPool = ClassPool.getDefault()

    TinyPngTransform(Project project) {
        //为了能够查找到Android相关类，需要把Android.jar包的路径添加到classPool 类搜索路径
        classPool.appendClassPath(project.android.bootClasspath[0].toString())

        classPool.importPackage("android.os.Bundle")
        classPool.importPackage("android.widget.Toast")
        classPool.importPackage("android.app.Activity")

    }

    @Override
    String getName() {
        return "TinyPngTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        //该transform接收那些内容作为输入参数
        //CLASSES(0x01)
        //RESOURCES(0x02)，assets/目录下的资源，而不是res下的资源
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        //作用域
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        //1.对input --> directory --> class文件进行遍历
        //2.对input --> jar --> class文件进行遍历
        //3. 符合我们项目的包名，并且class文件的路径包含Activity.class结尾，还不能是buildconfig.class,R.class $.class

        def outputProvider = transformInvocation.outputProvider

        transformInvocation.inputs.each { input ->

            input.directoryInputs.each { dirInput ->
                println("dirInput abs file path: " + dirInput.file.absolutePath)
                handleDirectory(dirInput.file)


                //把input->dir->class-> dest目标目录下面
                def dest = outputProvider.getContentLocation(dirInput.name, dirInput.contentTypes, dirInput.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(dirInput.file, dest)
            }

            input.jarInputs.each { jarInputs ->
                println("jarInputs abs file path: " + jarInputs.file.absolutePath)
                //对jar修改完之后，会返回一个新的jar文件
                def scrFile = handleJar(jarInputs.file)

                //主要是为了防止重名
                def jarName = jarInputs.name
                def md5 = DigestUtils.md5Hex(jarInputs.file.absolutePath)
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
                //获取jar包的输出路径
                def dest = outputProvider.getContentLocation(md5 + jarName, jarInputs.contentTypes, jarInputs.scopes, Format.JAR)
                FileUtils.copyDirectory(scrFile, dest)
            }
        }

    }

    //处理当前目录下所有的class文件
    void handleDirectory(File dir) {
        classPool.appendClassPath(dir.absolutePath)
        if (dir.isDirectory()) {
            dir.eachFileRecurse { file ->
                def filePath = file.absolutePath
                println("handleDirectory file path" + filePath)
                if (shouldModifyClass(filePath)) {
                    def inputStream = new FileInputStream(file)
                    modifyClass(inputStream)
                }
            }
        }
    }

    File handleJar(File jarFile) {}

    boolean shouldModifyClass(String filePath) {
        return (filePath.contains("")
                && filePath.endsWith("Activity.class")
                && !filePath.contains("R.class")
                && !filePath.contains('$')
                && !filePath.contains('$R')
                && !filePath.contains("BuildConfig.class"))
    }

    void modifyClass(InputStream inputStream) {

    }
}