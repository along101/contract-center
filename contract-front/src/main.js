import Vue from 'vue';
import $ from "jquery";

import router from './router';
import ElementUI from 'element-ui';
// import 'element-ui/lib/theme-chalk/index.css';
import commonPlugin from '~/common/commonPlugin';
// import './assets/css/theme-green/index.css';
// import './assets/css/theme-green/color-green.css';

import './assets/css/blackblue/index.css'

import './assets/css/main.css';

import './assets/css/sons-of-obsidian.css'

import store from './store'
import axios from 'axios'
import jwtTokenUtil from "./utils/jwtTokenUtil";


import Vuex from 'vuex'
import './common/prettify.js'
import './common/lang-proto.js'
//import VueResource from "vue-resource"
//Vue.use(VueResource)
Vue.use(Vuex)

Vue.prototype.$http = axios;

Vue.use(ElementUI);
Vue.use(commonPlugin);

// http request 拦截器
axios.interceptors.request.use(
    config => {
        console.log(config.method)
        let jwtToken = jwtTokenUtil.readAccess();
        // 给http请求的header加上jwt-token
        config.headers['jwt-token'] = jwtToken;
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

new Vue({
    store,
    router,
}).$mount('#app');

