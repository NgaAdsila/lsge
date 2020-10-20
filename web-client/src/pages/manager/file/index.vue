<template>
  <div id="manager-file-page">
    <ManagerFileListComponent
        :fileList="fileList"
        :fileListError="fileListError"
        :chooseFile="chooseFileId"
        :isLoading="isLoading"
        @getFileList="getFileList"
        @selectFile="selectFile"
        @resetData="resetData"
    />
    <ManagerFilePreviewComponent
        :fileList="fileList"
        :chooseFileId="chooseFileId"
        :chooseFile="chooseFileData" />
  </div>
</template>

<script>
import ManagerFileListComponent from "@/components/manager/file/FileListComponent";
import ManagerFilePreviewComponent from "@/components/manager/file/FilePreviewComponent";
import {getList, readFile} from "@/services/file_service";
import {RESPONSE} from "@/services/constants";
export default {
  name: "ManagerFile",
  components: {ManagerFilePreviewComponent, ManagerFileListComponent},
  data() {
    return {
      fileList: {
        root: '',
        files: []
      },
      isLoading: false,
      fileListError: null,
      chooseFileId: null,
      chooseFileData: null
    }
  },
  methods: {
    async getFileList(filePath) {
      try {
        this.isLoading = true
        this.resetData()
        const res = await getList({
          filePath: filePath
        });
        if (res.status === RESPONSE.STATUS.SUCCESS) {
          this.fileList.root = res.data.root;
          if (res.data.files) {
            this.fileList.files = res.data.files
          }
        } else {
          this.fileListError = res.message
        }
      } catch (e) {
        console.log('Get file list error: ', e)
      } finally {
        this.isLoading = false
      }
    },
    resetData() {
      this.fileList = {
        root: '',
        files: []
      }
      this.chooseFileId = null
      this.chooseFileData = null
      this.fileListError = null
    },
    async selectFile(index) {
      if (index >= this.fileList.files.length) {
        return
      }
      try {
        this.chooseFileId = index
        this.chooseFileData = ''
        const res = await readFile(this.fileList.files[index].path, this.fileList.files[index].extension);
        if (res.status === RESPONSE.STATUS.SUCCESS) {
          this.chooseFileData = res.data
        }
      } catch (e) {
        console.log('Get file detail error: ', e)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
#manager-file-page {
  display: flex;
  flex-direction: row;
  min-width: 500px;
  height: calc(100vh - 12.5rem);
  #manager-file-list-component {
    width: 20%;
    height: 100%;
    padding-right: 0.5rem;
    margin-right: 0.5rem;
    border-right: 5px double #dbdbdb;
  }
  #manager-file-preview-component {
    width: 80%;
    height: 100%;
  }
}
</style>