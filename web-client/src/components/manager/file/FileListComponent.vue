<template>
  <div id="manager-file-list-component">
    <div class="file-list-header">
      {{ $t('manager.file.label.list-header') | uppercase }}
      <b-icon icon="arrow-repeat" variant="primary"
              scale="1.25"
              :animation="isLoading ? 'spin-reverse' : ''"
              class="has-link file-list-back" @click="resetData">
        Change text
      </b-icon>
    </div>
    <div class="file-list-content mt-2">
      <div v-if="fileList.files && fileList.files.length">
        <div v-for="(file, index) in fileList.files" :key="index">
          <div :class="(file.folder ? ' is-folder' : ' is-file has-link') + (index === chooseFile ? ' font-weight-bold' : '')"
               class="file-list-item"
               :style="{ 'margin-left': `${0.75 * file.deep}rem` }"
               @click="selectFile(file, index)"
          >
            <span v-if="file.folder" class="mr-1">
              <b-icon icon="folder2-open"></b-icon>
            </span>
              <span v-else class="mr-1">
              <b-icon icon="file-richtext"></b-icon>
            </span>
            <span>{{ file.name }}</span>
            <span v-if="file.deep === 0" class="font-italic file-root">{{ ` (${file.path})` }}</span>
          </div>
        </div>
      </div>
      <div v-else>
        <b-alert variant="warning" show>{{ fileListError ? fileListError : $t('manager.file.label.list-empty') }}</b-alert>
        <div class="add-file">
          <b-form-input type="text"
                        required
                        autofocus
                        :placeholder="$t('manager.file.label.add-input-placeholder')"
                        v-model="filePath"
                        @keypress.enter.exact="getFileList"
          />
          <b-button variant="outline-primary"
                    class="mt-2"
                    @click="getFileList">
            {{ $t('manager.file.label.add-button') }}
          </b-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ManagerFileListComponent",
  props: [
      'fileList',
      'fileListError',
      'chooseFile',
      'isLoading'
  ],
  data() {
    return  {
      filePath: ''
    }
  },
  methods: {
    getFileList() {
      this.$emit('getFileList', this.filePath)
    },
    selectFile(file, index) {
      if (file.folder) {
        return
      }
      this.$emit('selectFile', index)
    },
    resetData() {
      this.filePath = ''
      this.$emit('resetData')
    }
  }
}
</script>

<style lang="scss" scoped>
#manager-file-list-component {
  .file-list-header {
    height: 2.5rem;
    line-height: 2.5rem;
    font-weight: 600;
    border-bottom: 3px double #dbdbdb;
    position: relative;
    .file-list-back {
      position: absolute;
      right: 0;
      top: 0.75rem;
    }
  }
  .file-list-content {
    height: calc(100% - 2.75rem);
    overflow-y: auto;
    .file-list-item {
      line-height: 1.75rem;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      .file-root {
        font-size: 80%;
        opacity: 0.8;
      }
    }
  }
}
</style>