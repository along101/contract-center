import {api} from '../../api'

const state = {
    usersByName : ''
};

const getters = {
    getUsersByName : state => state.usersByName,
    isAppAdmin: (state,getters) => {
        console.log('isAppAdmin')
        let appInfo = getters.getProjectsByAppId;
        let userName = getters.getUserName;
        if (appInfo && appInfo.userDtos) {
            //遍历 userDtos, 判断 userName 是否存在

            let arr = appInfo.userDtos;

            for(let j = 0; j < arr.length; j++) {
                if(userName === arr[j].userName) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }
};

const actions = {
    //名字模糊查询
    queryUsersByUserName({commit}, data) {
        api.userService.fuzzySearchUsersByName(data).then(function (res) {
            commit("updateUsersByNameList", res.data.detail);
        }.bind(this)).catch(function (err) {
            console.log(err);
        }.bind(this));
    },
};

const mutations = {
    updateUsersByNameList(state, data) {
        state.usersByName = data;
    },
};

export default {
    state,
    getters,
    actions,
    mutations
}
