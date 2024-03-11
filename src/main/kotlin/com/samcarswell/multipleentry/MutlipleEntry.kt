package com.samcarswell.multipleentry

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.Messages
import kotlin.math.absoluteValue


class MultipleEntry : AnAction() {
    private fun ordinalOf(i: Int): String {
        val iAbs = i.absoluteValue
        return "$i" + if (iAbs % 100 in 11..13) "th" else when (iAbs % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.getData(PlatformDataKeys.PROJECT)
        val editor: Editor? = event.getData(CommonDataKeys.EDITOR)
        val document: Document = editor!!.document

        editor.caretModel.allCarets.forEachIndexed { index, caret ->
            val start: Int = caret.selectionStart
            val end: Int = caret.selectionEnd
            val replaceText : String = Messages.showInputDialog(
                    "Enter value for ${ordinalOf(index+1)} cursor",
                    "Multiple Entry",
                    Messages.getQuestionIcon()
            ).toString()
            WriteCommandAction.runWriteCommandAction(project) {
                document.replaceString(start, end, replaceText)
            }
        }
    }
}