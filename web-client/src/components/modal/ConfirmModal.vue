<template>
    <b-modal id="modal-confirm"
             centered
             :title="$t('common.modal.confirm_title')"
             @ok="onOk"
             @cancel="cancel">
        <p class="my-4 text-center">{{ message }}</p>
    </b-modal>
</template>

<script>
    export default {
        name: "ConfirmModal",
        props: [
            'message'
        ],
        methods: {
            onOk() {
                this.$emit('onOk');
            },
            cancel() {
                this.$emit('cancel');
            }
        }
    }
</script>

<style scoped>

</style>