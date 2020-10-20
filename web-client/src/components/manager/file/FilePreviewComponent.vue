<template>
  <div id="manager-file-preview-component">
    <div class="file-list-header">
      {{ $t('manager.file.label.preview-header') | uppercase }}
    </div>
    <div class="file-list-content mt-2">
      <div v-if="chooseFile">
        <b-img v-if="fileType === 'image'" :src="generatePdfFile('image/png')" class="image-viewer"></b-img>
        <iframe v-else-if="fileType === 'pdf'" type="application/pdf" :src="generatePdfFile()" class="pdf-viewer"></iframe>
        <VueDocPreview v-else-if="fileType !== ''" :value="chooseFile | json_pretty" :type="fileType" />
        <b-alert v-else variant="warning" show>{{ $t('manager.file.label.cant_read') }}</b-alert>
      </div>
    </div>
  </div>
</template>

<script>
import VueDocPreview from 'vue-doc-preview';
import {ALLOWED_IMAGE_FILE_EXTENSIONS} from "@/services/constants";
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
      if (extension === 'pdf') {
        return 'pdf'
      }
      if (ALLOWED_IMAGE_FILE_EXTENSIONS.includes(extension)) {
        return 'image'
      }
      if (extension === 'md') {
        return 'markdown'
      }
      return 'text'
    },

  },
  methods: {
    generatePdfFile(type = 'application/pdf') {
      return URL.createObjectURL(new Blob([this.chooseFile], {type: type}))
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
    .pdf-viewer {
      width: 64vw;
      height: calc(100vh - 250px);
    }
    .image-viewer {
      background-color: rgba(0,0,0,0.4);
      max-width: 50%;
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