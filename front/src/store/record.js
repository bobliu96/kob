export default {
  state: {
    is_record: false,
    a_steps: "",
    b_steps: "",
    record_loser: "",
    a_photo: "",
    a_username: "",
    a_win: "",
    a_lose: "",
    a_draw: "",
    b_photo: "",
    b_username: "",
    b_win: "",
    b_lose: "",
    b_draw: "",
  },
  getters: {},
  mutations: {
    updateIsRecord(state, is_record) {
      state.is_record = is_record;
    },
    updateSteps(state, data) {
      state.a_steps = data.a_steps;
      state.b_steps = data.b_steps;
    },
    updateRecordLoser(state, record_loser) {
      state.record_loser = record_loser;
    },
    updatePlayerInfo(state, player_info) {
      state.a_photo = player_info.a_photo;
      state.a_username = player_info.a_username;
      state.a_win = player_info.a_win;
      state.a_lose = player_info.a_lose;
      state.a_draw = player_info.a_draw;
      state.b_photo = player_info.b_photo;
      state.b_username = player_info.b_username;
      state.b_win = player_info.b_win;
      state.b_lose = player_info.b_lose;
      state.b_draw = player_info.b_draw;
    },
  },
  actions: {},
  modules: {},
};
