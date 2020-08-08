package ru.sadv1r.idea.plugin.ansible.vault.editor

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import org.jetbrains.yaml.YAMLFileType
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLScalarList
import ru.sadv1r.ansible.vault.VaultHandler

class PropertyVault(
    private val document: Document,
    private val property: YAMLKeyValue
) : Vault() {
    override fun setEncryptedData(data: String) {
        WriteCommandAction.runWriteCommandAction(property.project) {
            (property.value as YAMLScalarList).updateText(data)
        }
    }

    override fun isEmpty(): Boolean {
        return property.valueText.isEmpty()
    }

    override fun getDecryptedData(password: String): ByteArray {
        return VaultHandler.decrypt(property.valueText, password)
    }

    override fun getKey(): String {
        val path = FileDocumentManager.getInstance().getFile(document)!!.path
        return "${path}:${property.keyText}"
    }

    override fun getFileType(): YAMLFileType {
        return YAMLFileType.YML
    }
}