import i18n from '../plugins/i18n'
import Vue from 'vue'
import {VARIANT} from "@/services/constants"

const vue = new Vue()

export default {
    notify: (message, variant = VARIANT.INFO, link = '') => {
        vue.$bvToast.toast(message, {
            title: i18n.t('common.toast.notify_title'),
            toaster: 'b-toaster-bottom-right',
            solid: true,
            variant: variant,
            href: link,
            autoHideDelay: 3000
        });
    },
    message: (message, variant = VARIANT.SUCCESS) => {
        vue.$bvToast.toast(message, {
            title: i18n.t('common.toast.title'),
            toaster: 'b-toaster-top-center',
            solid: true,
            variant: variant,
            autoHideDelay: 2000
        });
    }
}