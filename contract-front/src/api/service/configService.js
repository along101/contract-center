import restApi from '../restApi'

export default {
   getCommonConfig(){
       let url = '/web/configs/common';
       return restApi.doGetRequest(url);
   }

}
