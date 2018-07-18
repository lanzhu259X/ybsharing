import cookie from "js-cookie";

const user = {
  state: {
    token: null,
    userDetail: null
  },
  mutations: {
    setToken(state, data) {
      localStorage.token = data;
      state.token = data;
    },
    setUserDetail(state, data) {
      if (!data) {
        localStorage.userDetail = null;
        state.userDetail = null;
      } else {
        if (typeof data === "string") {
          let json = JSON.parse(data);
          localStorage.userDetail = JSON.stringify(json);
          state.userDetail = json;
        } else {
          localStorage.userDetail = JSON.stringify(data);
          state.userDetail = data;
        }
      }
    },
    logout(state, vm) {
      cookie.remove("token");
      localStorage.token = null;
      localStorage.userDetail = null;
      state.token = null;
      state.userDetail = null;
      localStorage.clear();
    }
  }
};

export default user;
