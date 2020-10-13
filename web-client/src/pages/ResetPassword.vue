<template>
  <b-container>
    <b-overlay :show="isSubmit" rounded="sm" spinner-variant="primary">
      <ResetPasswordComponent
          :isValidLink="isValidLink"
          :invalidMessage="invalidMessage"
          :isSubmit="isSubmit"
          @resetPassword="onResetPassword"/>
    </b-overlay>
  </b-container>
</template>

<script>
import {initResetPassword, resetPassword} from '@/services/user_service';
import {RESPONSE, VARIANT} from "@/services/constants";
import ToastHelper from "@/helper/ToastHelper";
import ResetPasswordComponent from "@/components/ResetPasswordComponent";
export default {
  name: "Register",
  components: {ResetPasswordComponent},
  data() {
    return {
      isSubmit: false,
      isValidLink: true,
      invalidMessage: null
    }
  },
  computed: {
    token: function () {
      return this.$route.query.token
    },
    email: function () {
      return decodeURI(this.$route.query.email || '')
    }
  },
  mounted() {
    this.initResetPassword();
  },
  methods: {
    async initResetPassword() {
      try {
        const res = await initResetPassword({
          email: this.email,
          token: this.token
        })
        if (res.status === RESPONSE.STATUS.SUCCESS) {
          this.isValidLink = true
          this.invalidMessage = null
        } else {
          this.isValidLink = false
          this.invalidMessage = res.message
        }
      } catch (e) {
        this.isValidLink = false
        this.invalidMessage = null
      }
    },
    async onResetPassword(data = {}) {
      try {
        this.isSubmit = true;
        data.email = this.email
        data.token = this.token
        const res = await resetPassword(data)
        if (res.status === RESPONSE.STATUS.SUCCESS) {
          ToastHelper.message(res.data.message)
          setTimeout(async (self = this) => {
            await self.$router.push('/login')
            this.isSubmit = false
          }, 1000)
        } else {
          this.isSubmit = false
          ToastHelper.message(res.message, VARIANT.DANGER)
        }
      } catch (e) {
        console.log('Reset password ERROR: ', e)
        this.isSubmit = false
      }
    }
  }
}
</script>

<style scoped>

</style>