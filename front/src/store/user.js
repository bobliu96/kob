import $ from "jquery";

export default {
  state: {
    id: "",
    username: "",
    photo: "",
    token: "",
    firstName: "",
    lastName: "",
    emailAddress: "",
    win: "",
    lose: "",
    draw: "",
    is_login: false,
    pulling_info: true, //whether pulling information or not
  },
  getters: {},
  mutations: {
    updateUser(state, user) {
      state.id = user.id;
      state.username = user.username;
      state.photo = user.photo;
      state.firstName = user.firstName;
      state.lastName = user.lastName;
      state.emailAddress = user.emailAddress;
      state.win = user.win;
      state.lose = user.lose;
      state.draw = user.draw;
      state.is_login = user.is_login;
    },

    updateToken(state, token) {
      state.token = token;
    },

    logout(state) {
      state.id = "";
      state.username = "";
      state.photo = "";
      state.firstName = "";
      state.lastName = "";
      state.emailAddress = "";
      state.win = "",
      state.lose = "",
      state.draw =  "",
      state.token = "";
      state.is_login = false;
    },
    updatePullingInfo(state, pulling_info) {
      state.pulling_info = pulling_info;
    },
  },
  actions: {
    login(context, data) {
      $.ajax({
        url: "http://127.0.0.1:3000/user/account/token/",
        type: "POST",
        data: {
          username: data.username,
          password: data.password,
        },
        success: (res) => {
          if (res.error_message === "success") {
            localStorage.setItem("jwt_token", res.token);
            context.commit("updateToken", res.token);
            data.success(res);
          } else {
            data.error(res);
          }
        },
        error: (err) => {
          data.error(err);
        },
      });
    },

    getInfo(context, data) {
      $.ajax({
        url: "http://127.0.0.1:3000/user/account/info/",
        type: "GET",
        headers: {
          Authorization: "Bearer " + context.state.token,
        },
        success: (res) => {
          if (res.error_message === "success") {
            context.commit("updateUser", {
              ...res,
              is_login: true,
            });
            data.success(res);
          } else {
            data.error(res);
          }
        },
        error: (err) => {
          data.error(err);
        },
      });
    },

    logout(context) {
      localStorage.removeItem("jwt_token");
      context.commit("logout");
    },
  },
  modules: {},
};
