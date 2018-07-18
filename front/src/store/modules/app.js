const app = {
  state: {
    avator: null
  },
  mutations: {
    setAvator(state, data) {
      localStorage.avator = data;
      state.avator = data;
    }
  },
  actions: {}
};

export default app;
