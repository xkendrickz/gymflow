import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import Toast from 'vue-toastification'
import 'vue-toastification/dist/index.css'
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import '@mdi/font/css/materialdesignicons.css'
import { useAuthStore } from '@/stores/auth'
import '@/assets/shared.css' // ← add this

const vuetify = createVuetify({ components, directives })
const pinia = createPinia()
const app = createApp(App)

app.use(vuetify)
app.use(pinia)
app.use(Toast)

const auth = useAuthStore()
auth.init()

app.use(router)
app.mount('#app')
