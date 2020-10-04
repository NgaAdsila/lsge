<template>
    <b-modal id="chat-detail-set-nickname-modal"
             centered
             :title="$t('chatroom.label.set_nickname')"
             :ok-title="$t('common.label.save')"
             :ok-disabled="$v.$error"
             @ok="onOk"
             @cancel="cancel">
        <div v-if="nicknames && nicknames.length">
            <b-form-group v-for="(user, index) in nicknames"
                          label-size="sm"
                          :label="user.name"
                          :key="index"
                          :label-for="'input-name-' + index">
                <b-form-input :id="'input-name' + index" size="sm"
                              v-model="$v.user.nickname.$model"
                              maxlength="100"
                              :state="validateState('nicknames', index)"
                              :aria-describedby="'name-live-feedback' + index"
                              ref="name"
                              class="grey-text"></b-form-input>
                <b-form-invalid-feedback :id="'name-live-feedback' + index">
                    {{ $t('common.validation.max', { name: $t('chatroom.label.nickname'), max: 100 }) }}
                </b-form-invalid-feedback>
            </b-form-group>
        </div>
    </b-modal>
</template>

<script>
    import { validationMixin } from 'vuelidate';
    import {maxLength} from 'vuelidate/lib/validators';

    export default {
        name: "ChatDetailSetNickname",
        mixins: [validationMixin],
        props: [
            'currentUsers'
        ],
        data() {
            return {
                nicknames: []
            }
        },
        watch: {
            currentUsers: function(users) {
                this.nicknames = users
            }
        },
        validations: {
            nicknames: {
                nickname: {
                    maxLength: maxLength(100)
                }
            }
        },
        methods: {
            validateState(name, index) {
                console.log(name, index, this.$v)
                // const { $dirty, $error } = this.$v[name]
                // return $dirty ? !$error : null
            },
            onOk() {
                if (this.$v.$error) {
                    return false
                }
                this.$emit('onOk')
            },
            cancel() {
                this.$emit('cancel')
            }
        }
    }
</script>

<style scoped>

</style>