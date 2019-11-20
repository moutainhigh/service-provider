import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import {
  Toast,
  Picker,
  Image,
  List,
  Button,
  Area,
  Uploader,
  Checkbox,
  CheckboxGroup,
  Row,
  Col,
  Icon,
  Tab,
  Tabs,
  DatetimePicker,
  Popup,
  NavBar,
  Field,
  Search,
  Cell,
  CellGroup
} from 'vant'
import './style/common/base.styl'
/* import './plugins/element.js' */
Vue.config.productionTip = false;

// 全局注册
Vue.use(Toast).use(Picker).use(Image).use(Cell).use(CellGroup).use(List).use(Icon).use(Tab).use(Tabs).use(Row).use(Col).use(Uploader).use(Button).use(Area).use(Checkbox).use(CheckboxGroup).use(DatetimePicker).use(Popup).use(NavBar).use(Field).use(Search);

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
router.beforeEach((to, from, next) => {
  console.log(to);
  if(to.path=='/merchant'){
    
  }
  
})