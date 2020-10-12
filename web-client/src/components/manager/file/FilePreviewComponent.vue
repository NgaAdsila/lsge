<template>
  <div id="manager-file-preview-component">
    <div class="file-list-header">
      {{ $t('manager.file.label.preview-header') | uppercase }}
    </div>
    <div class="file-list-content mt-2">
      <div v-if="chooseFile">
        <VueDocPreview v-if="fileType !== ''" :value="chooseFile" :type="fileType" />
        <b-alert v-else variant="warning" show>Not show file</b-alert>
      </div>
    </div>
  </div>
</template>

<script>
import VueDocPreview from 'vue-doc-preview';
export default {
  name: "ManagerFilePreviewComponent",
  components: { VueDocPreview },
  props: [
    'fileList',
    'chooseFile',
    'chooseFileId'
  ],
  computed: {
    fileType: function () {
      if (!this.chooseFileId || !this.fileList.files[this.chooseFileId]) {
        return 'text'
      }
      let extension = this.fileList.files[this.chooseFileId].extension
      if (!extension || extension === '') {
        return 'text'
      }
      extension = extension.toLowerCase()
      if (extension === 'md') {
        return 'markdown'
      }
      if (extension === 'docx') {
        return 'office'
      }
      if (extension === 'pdf') {
        return 'pdf'
      }
      return 'text'
    }
  }
}
</script>

<style lang="scss" scoped>
#manager-file-preview-component {
  .file-list-header {
    height: 2.5rem;
    line-height: 2.5rem;
    font-weight: 600;
    border-bottom: 3px double #dbdbdb;
  }
  .file-list-content {
    height: calc(100% - 2.75rem);
    overflow: auto;
  }
}
</style>

<style lang="scss">
#manager-file-preview-component {
  pre {
    font-size: 87.5% !important;
    white-space: pre-wrap;
  }
}
</style>