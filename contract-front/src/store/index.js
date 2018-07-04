/**
 * Created by lizhiming on 2017/8/17.
 */
import Vue from 'vue'
import Vuex from 'vuex'
import projectStore from './model/projectStore'
import apiDocumentStore from './model/apiDocumentStore'
import pauthStore from './model/pauth'
import userStore from './model/userStore'
import protoFileStore from './model/protoFileStore'
import configStore from './model/configStore'
import versionStore from './model/versionStore'


Vue.use(Vuex);

const debug = process.env.NODE_ENV !== 'production';

export default new Vuex.Store({
    modules: {
        projectStore,
        apiDocumentStore,
        pauthStore,
        userStore,
        protoFileStore,
        configStore,
        versionStore
    },
    strict: debug
});

