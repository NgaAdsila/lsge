<template>
    <b-modal
            id="create-post-modal"
            scrollable
            :title="$t('post.label.create_post')"
            size="lg"
            centered
            @show="resetModal"
            @hidden="resetModal"
            @ok="handleOk"
    >
        <b-form @submit.stop.prevent="onCreatePost">
            <b-form-group label-size="sm"
                          :label="$t('post.label.title')"
                          label-for="input-title">
                <b-form-input id="input-title" size="sm"
                              v-model="$v.title.$model"
                              maxlength="255"
                              :state="validateState('title')"
                              aria-describedby="title-live-feedback"
                              ref="title"
                              class="grey-text"></b-form-input>
                <b-form-invalid-feedback id="title-live-feedback">
                    {{ !$v.title.required
                    ? $t('common.validation.required', { name: $t('post.label.title') })
                    : $t('common.validation.max', { name: $t('post.label.title'), max: 255 }) }}
                </b-form-invalid-feedback>
            </b-form-group>
            <b-form-group label-size="sm"
                          :label="$t('post.label.content')"
                          label-for="input-content">
                <b-form-textarea id="input-content" size="sm"
                                 v-model="$v.content.$model"
                                 :state="validateState('content')"
                                 rows="5"
                                 max-rows="20"
                                 aria-describedby="content-live-feedback"
                                 ref="content"
                                 class="grey-text"></b-form-textarea>
                <b-form-invalid-feedback id="content-live-feedback">
                    {{ $t('common.validation.required', { name: $t('post.label.content') }) }}
                </b-form-invalid-feedback>
            </b-form-group>
            <b-form-group label-size="sm"
                          :label="$t('post.label.shareMode')"
                          label-for="input-shareMode">
                <b-form-radio-group
                        id="input-shareMode"
                        v-model="$v.shareMode.$model"
                        name="radio-options"
                >
                    <b-form-radio v-for="(item, index) of shareModeOptions" :value="item.id" :key="'shareMode-item-' + index">
                        <b-icon :icon="item.icon" variant="success"></b-icon> {{ $t('post.shareMode.' + item.id) }}
                    </b-form-radio>
                </b-form-radio-group>
            </b-form-group>
        </b-form>
    </b-modal>
</template>

<script>
    import { validationMixin } from 'vuelidate';
    import { required, maxLength } from 'vuelidate/lib/validators';
    import {POST} from "../../../services/constants";

    export default {
        name: "CreatePostModal",
        mixins: [validationMixin],
        computed: {
            shareModeOptions() {
                return POST.SHARE_MODE_OPTIONS
            }
        },
        data() {
            return {
                title: null,
                content: null,
                shareMode: POST.SHARE_MODE.PUBLIC
            }
        },
        validations: {
            title: {
                required,
                maxLength: maxLength(255)
            },
            content: {
                required
            },
            shareMode: {
                required
            }
        },
        methods: {
            validateState(name) {
                const { $dirty, $error } = this.$v[name]
                return $dirty ? !$error : null
            },
            resetModal() {
                this.title = null
                this.content = null
                this.shareMode = POST.SHARE_MODE.PUBLIC
            },
            handleOk(bvModelEvt) {
                bvModelEvt.preventDefault()
                this.onUpdatePost()
            },
            onUpdatePost() {
                this.$v.$touch();
                if (this.$v.$anyError) {
                    this.focusFirstError();
                    return;
                }
                this.$emit('createPost', this.title, this.content, this.shareMode);
                this.$nextTick(() => {
                    this.$bvModal.hide('create-post-modal')
                })
            },
            focusFirstError() {
                const invalidFields = Object.keys(this.$v.$params)
                    .filter(fn => this.$v[fn].$invalid);
                if (invalidFields) {
                    this.$refs[invalidFields[0]].$el.focus();
                }
            }
        }
    }
</script>

<style scoped>

</style>