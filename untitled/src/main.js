import { createApp } from 'vue'
import App from './App.vue'
import router from "@/router/index.js";
import axios from "axios";
import '../src/assets/body.css'
import 'element-plus/dist/index.css'

axios.defaults.baseURL="http://localhost:8081"

const app=createApp(App)


app.use(router)

app.mount('#app')