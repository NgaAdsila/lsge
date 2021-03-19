<template>
    <b-modal :id="id || 'modal-confirm'"
             centered
             :title="$t('common.modal.confirm_title')"
             @ok="onOk"
             @cancel="cancel"
             @hide="hide">
        <p class="my-4 text-center">{{ message }}</p>
    </b-modal>
</template>

<script>
    export default {
        name: "ConfirmModal",
        props: [
            'id',
            'message',
            'data'
        ],
        methods: {
            onOk() {
                this.$emit('onOk', this.data);
            },
            cancel() {
                this.$emit('cancel');
            },
            hide() {
                this.$emit('hide');
            }
        }
    }
</script>

<style scoped>

</style>