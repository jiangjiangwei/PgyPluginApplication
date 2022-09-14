package com.example.gradle.mylibrary.task

import com.example.gradle.mylibrary.*
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject

/**
 * author:江伟
 * e-mail:jiangwei@sip.sh.cn
 * time  : 2022/09/08
 * desc  : 加固任务 ---360免费用户无法使用命令加固
 * version:  1.0
 */
open class ReinforceTask @Inject constructor(
    private val outputFile: File,
    private val uploadExtension: UploadExtension
) : DefaultTask() {
    init {
        description = "Reinfoce Release Apk"
        // 定义该task所属的group，如果不定义group的话task会在other分组里
        group = UPLOAD_EXTENSION
    }

    private val bashCmd by lazy { "java -jar ${uploadExtension.reinforceFilePath}" }

    //登录
    private val loginCmd by lazy { "$bashCmd -login ${uploadExtension.reinforceUserName} ${uploadExtension.reinforcePassword}" }

    //导入签名
    private val signCmd by lazy { "$bashCmd -importsign ${uploadExtension.signingConfig.sign()}" }

    private val showSignCmd by lazy { "$bashCmd -showsign " }

    private val showmulpkgCmd by lazy { "$bashCmd -showmulpkg " }

    private val showConfigCmd by lazy { "$bashCmd -showconfig " }


    // @TaskAction 注解表示该方法是task执行的入口，当双击该task的时候就会执行里面的逻辑。
    @TaskAction
    fun action() {
        //获取签名信息，便于后续重签
        val apkFilePath = outputFile.absolutePath
        log("apkFilePath=$apkFilePath")
        //360加固 登录
        log("加固======开始登录--$loginCmd")
        val execute = loginCmd.execute()
        val loginResult = execute.text()
        log("loginReuset=$loginResult")
        execute.waitFor() //等待外部进程调用结束


        log("加固======导入签名--$signCmd")
        signCmd.exeCmd()

        log("显示签名信息 =$showSignCmd")
        showSignCmd.exeCmd()

        log("显示配置信息 =$showConfigCmd")
        showConfigCmd.exeCmd()

        log("显示渠道信息=$showmulpkgCmd")
        showmulpkgCmd.exeCmd()


//        val jiaguCmd =
//            "$bashCmd -jiagu $apkFilePath ${uploadExtension.outputDirectory} -autosign -automulpkg -pkgparam ${uploadExtension.channelConfigFilePath}"
        val jiaguCmd = "$bashCmd -jiagu $apkFilePath ${uploadExtension.outputDirectory} -autosign"
        log("加固======开始加固-$jiaguCmd")
        jiaguCmd.exeCmd()
    }

    private fun String.exeCmd() {
        val e = this.execute()
        log("result = ${e.text()}")
        log("result error= ${e.error()}")
        e.waitFor()
    }
}