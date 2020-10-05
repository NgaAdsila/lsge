<template>
    <b-modal id="chat-detail-update-chatroom-modal"
             centered
             :title="$t('chatroom.label.update_chatroom')"
             :ok-title="$t('common.label.save')"
             :ok-disabled="$v.$error"
             @ok="onOk"
             @cancel="cancel">
        <b-form-group label-size="sm"
                      :label="$t('chatroom.label.name')"
                      label-for="input-name">
            <b-form-input id="input-name" size="sm"
                          v-model="$v.name.$model"
                          maxlength="100"
                          :state="validateState('name')"
                          aria-describedby="name-live-feedback"
                          ref="name"
                          class="grey-text"></b-form-input>
            <b-form-invalid-feedback id="name-live-feedback">
                {{ $t('common.validation.max', { name: $t('chatroom.label.name'), max: 100 }) }}
            </b-form-invalid-feedback>
        </b-form-group>
    </b-modal>
</template>

<script>
    import { validationMixin } from 'vuelidate';
    import {maxLength} from 'vuelidate/lib/validators';

    export default {
        name: "ChatDetailUpdateChatroom",
        mixins: [validationMixin],
        props: [
            'currentName'
        ],
        data() {
            return {
                name: ''
            }
        },
        watch: {
            currentName: function (val) {
                this.name = val
            }
        },
        validations: {
            name: {
                maxLength: maxLength(100)
            }
        },
        methods: {
            validateState(name) {
                const { $dirty, $error } = this.$v[name]
                return $dirty ? !$error : null
            },
            onOk() {
                this.$v.name.$touch()
                if (this.$v.name.$anyError) {
                    return false
                }
                this.$emit('onOk', this.name)
            },
            cancel() {
                this.$emit('cancel')
            }
        }
    }
</script>

<style scoped>

</style>