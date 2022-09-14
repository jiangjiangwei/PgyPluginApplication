package com.example.gradle.mylibrary

import com.android.build.gradle.AppExtension
import com.example.gradle.mylibrary.task.ReinforceTask
import com.example.gradle.mylibrary.task.UploadPgyTask
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * author:江伟
 * e-mail:jiangwei@sip.sh.cn
 * time  : 2022/09/07
 * desc  : this is PgyPlugin
 * version:  1.0
 */


class PgyPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        //创建uploadExtension对象
        val uploadExtension =
            project.extensions.create(UPLOAD_EXTENSION, UploadExtension::class.java)
        println("uploadExtension======$uploadExtension")
        //build.gradle文件解析完成会回调project.afterEvaluate，获取build.gradle中闭包的配置
        project.afterEvaluate {
            //AppExtension是Android插件创建的扩展-固定写法
            val appExtension = project.extensions.getByType(AppExtension::class.java)
            // 获取apk包的变体，applicationVariants默认有debug跟release两种变体
            appExtension.applicationVariants.all { applicationVariant ->
                applicationVariant.outputs.all { output ->
                    //构建加固任务--免费用户被限制使用命令加固
//                    project.tasks.create(
//                        REINFORCE_TASK + output.name,
//                        ReinforceTask::class.java,
//                        output.outputFile,
//                        uploadExtension
//                    ).dependsOn("assembleDebug")

                    //构建上传任务
                    val uploadPgyTask = project.tasks.create(
                        TASK_UPLOAD_PGY + output.name,
                        UploadPgyTask::class.java,
                        uploadExtension,
                        applicationVariant,
                        output.outputFile
                    )
                    applicationVariant.assembleProvider.get()
                        .dependsOn(project.tasks.findByName(TASK_CLEAN))
                    uploadPgyTask.dependsOn(applicationVariant.assembleProvider.get())
                }
            }
        }
    }
}