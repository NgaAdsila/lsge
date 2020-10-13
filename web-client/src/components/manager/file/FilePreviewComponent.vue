<template>
  <div id="manager-file-preview-component">
    <div class="file-list-header">
      {{ $t('manager.file.label.preview-header') | uppercase }}
    </div>
    <div class="file-list-content mt-2">
      <div v-if="chooseFile">
        <VueDocPreview v-if="fileType !== ''" :value="chooseFile | json_pretty" :type="fileType" />
        <b-alert v-else variant="warning" show>{{ $t('manager.file.label.cant_read') }}</b-alert>
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
      if (!extension || extension === '' || extension.toLowerCase() !== 'md') {
        return 'text'
      }
      return 'markdown'
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
    overscroll-behavior: contain;

    &::-webkit-scrollbar {
      width: 0.5rem;
    }

    &::-webkit-scrollbar-track {
      -webkit-box-shadow: inset 0 0 0.3rem transparent;
    }

    &::-webkit-scrollbar-thumb {
      background-color: transparent;
      border-radius: 0.5rem;
      outline: none;
    }

    &:hover::-webkit-scrollbar-thumb {
      background-color: darkgrey;
    }
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