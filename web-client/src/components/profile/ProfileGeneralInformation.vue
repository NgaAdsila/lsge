<template>
    <b-form @submit.stop.prevent="save" class="profile-form">
        <b-form-group label-size="sm"
                      :label="$t('common.label.username')"
                      label-for="input-username">
            <b-form-input id="input-username" size="sm"
                          v-model="$v.profileModel.username.$model"
                          maxlength="40"
                          readonly
                          disabled></b-form-input>
        </b-form-group>
        <b-form-group label-size="sm"
                      :label="$t('common.label.name')"
                      label-for="input-name">
            <b-form-input id="input-name" size="sm"
                          v-model="$v.profileModel.name.$model"
                          maxlength="40"
                          :state="validateState('name')"
                          aria-describedby="name-live-feedback"
                          ref="name"></b-form-input>
            <b-form-invalid-feedback id="name-live-feedback">
                {{ $t('common.validation.required', { name: $t('common.label.name') }) }}
            </b-form-invalid-feedback>
        </b-form-group>
        <b-form-group label-size="sm"
                      :label="$t('common.label.email')"
                      label-for="input-email">
            <b-form-input type="email" id="input-email" size="sm"
                          v-model="$v.profileModel.email.$model"
                          :state="validateState('email')"
                          aria-describedby="email-live-feedback"
                          ref="email"></b-form-input>
            <b-form-invalid-feedback id="email-live-feedback">
                {{ !$v.profileModel.email.required
                ? $t('common.validation.required', { name: $t('common.label.email') })
                : $t('common.validation.invalid_email') }}
            </b-form-invalid-feedback>
        </b-form-group>
        <b-form-group label-size="sm"
                      :label="$t('common.label.color')"
                      label-for="input-color">
            <b-form-input type="color" id="input-color" size="sm"
                          v-model="$v.profileModel.color.$model"
                          ref="color"></b-form-input>
        </b-form-group>
        <b-form-group label-size="sm"
                      :label="$t('common.label.avatar')"
                      label-for="input-avatarFile">
            <b-form-file id="input-avatarFile"
                         v-model="$v.profileModel.avatarFile.$model"
                         :placeholder="$t('common.placeholder.image', { types: allowedExtensionTypes })"
                         @change="onAvatarFileChange"
                         :state="validateStateAvatar"
                         aria-describedby="avatarFile-live-feedback"
                         ref="avatarFile"></b-form-file>
            <b-form-invalid-feedback id="avatarFile-live-feedback">
              {{ avatarFileError.imageType
                    ? $t('common.validation.invalid_image_type', {types: allowedExtensionTypes})
                    : $t('common.validation.invalid_image_size', {name: $t('common.label.avatar'), max: maxFileSize}) }}
            </b-form-invalid-feedback>
            <div v-show="profileModel.avatar" id="avatar-preview">
                <b-img :src="profileModel.avatar || ''" />
            </div>
        </b-form-group>
        <div class="text-center mt-4">
            <b-button type="submit"
                      :disabled="!!isLoading || $v.$anyError || !validateStateAvatar"
                      :readonly="!!isLoading || $v.$anyError || !validateStateAvatar"
                      variant="outline-primary">{{ $t('common.label.save') }}</b-button>
        </div>
    </b-form>
</template>

<script>
    import { validationMixin } from 'vuelidate';
    import { required, minLength, maxLength, email } from 'vuelidate/lib/validators';
    import { strictUserName } from '@/plugins/vuevalidate';
    import {FILE_UPLOAD} from "@/services/constants";

    export default {
        name: "ProfileGeneralInformation",
        mixins: [validationMixin],
        props: [
            'profile',
            'isLoading',
            'profileModel'
        ],
        validations: {
            profileModel: {
                name: {
                    required
                },
                username: {
                    required,
                    minLength: minLength(4),
                    maxLength: maxLength(40),
                    strictUserName
                },
                email: {
                    required,
                    email
                },
                color: {},
                avatarFile: {}
            }
        },
        data() {
            return {
                validateStateAvatar: true,
                avatarFileError: {
                    imageType: false,
                    imageSize: false
                }
            }
        },
        computed: {
            allowedExtensionTypes: function () {
                return FILE_UPLOAD.ALLOWED_EXTENSION_TITLE
            },
            maxFileSize: function () {
                return FILE_UPLOAD.MAX_SIZE_TITLE
            }
        },
        methods: {
            validateState(name) {
                const { $dirty, $error } = this.$v.profileModel[name];
                return $dirty ? !$error : null;
            },
            save() {
                this.$v.$touch();
                if (this.$v.$anyError || !this.validateStateAvatar) {
                    return;
                }
                this.$emit('save');
            },
            onAvatarFileChange(e) {
                const file = e.target.files[0]
                this.$emit('addFileToModel', file)
                if (!this.imageType(file)) {
                    this.validateStateAvatar = false
                    this.avatarFileError.imageType = true
                    this.avatarFileError.imageSize = false
                    return
                }
                if (!this.imageSize(file)) {
                    this.validateStateAvatar = false
                    this.avatarFileError.imageType = false
                    this.avatarFileError.imageSize = true
                    return
                }
                this.validateStateAvatar = true
                this.avatarFileError.imageType = false
                this.avatarFileError.imageSize = false
                this.$emit('updateAvatarLinkToModel', file)
            },
            imageType(value) {
                return !value || FILE_UPLOAD.ALLOWED_EXTENSION_REGEX.test(value.name.toLowerCase())
            },
            imageSize(value) {
                return !value || value.size <= FILE_UPLOAD.MAX_SIZE
            }
        }
    }
</script>

<style lang="scss" scoped>
.profile-form {
    #avatar-preview {
        margin-top: 0.5rem;
        img {
            width: 6rem;
            height: 6rem;
            border-radius: 100%;
            object-fit: cover;
        }
    }
}
</style>