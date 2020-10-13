<template>
  <div class="manager-layout-footer docs-border bd-footer">
    <p class="mb-2">{{ $t('footer.message.welcome') }}</p>
    <p class="mb-3">{{ $t('footer.message.copy_right', {version: 'v1.0', year: (new Date()).getFullYear()}) }}</p>
  </div>
</template>

<script>
export default {
  name: "ManagerFooter"
}
</script>

<style lang="scss" scoped>
  .docs-border {
    border-top: 3px solid #dee2e6;
  }
  .bd-footer {
    margin-top: 3rem;
    padding: 0.5rem 0;
    text-align: center;
    background-color: rgba(0,0,0,0.2);
    font-size: .875rem;
  }
</style>