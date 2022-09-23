<template>
  <div class="container">
    <div class="row">
      <div class="col-2">
        <div class="card" style="margin: 20px auto">
          <div class="card-body">
            <img :src="a_photo" alt="" style="width: 100%" />
            <div class="playground-username" style="color: blue">
              {{ a_username }} (Blue)
            </div>
            <div class="user-history" style="color: blue">
              Win: {{ a_win }}<br />
              Lose: {{ a_lose }}<br />
              Draw: {{ a_draw }}
            </div>
          </div>
        </div>
      </div>
      <div class="col-8">
        <div class="playground" style=""><GameMap /></div>
      </div>
      <div class="col-2">
        <div class="card" style="margin: 20px auto">
          <div class="card-body">
            <img :src="b_photo" alt="" style="width: 100%" />
            <div class="playground-username" style="color: red">
              {{ b_username }} (Red)
            </div>
            <div class="user-history" style="color: red">
              Win: {{ b_win }}<br />
              Lose: {{ b_lose }}<br />
              Draw: {{ b_draw }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import GameMap from "./GameMap.vue";
import { useStore } from "vuex";
import { ref } from "vue";

export default {
  components: {
    GameMap,
  },

  setup() {
    const store = useStore();
    let a_username = ref("");
    let a_photo = ref("");
    let a_win = ref("");
    let a_lose = ref("");
    let a_draw = ref("");

    let b_username = ref("");
    let b_photo = ref("");
    let b_win = ref("");
    let b_lose = ref("");
    let b_draw = ref("");

    if (store.state.record.is_record) {
      a_username.value = store.state.record.a_username;
      a_photo.value = store.state.record.a_photo;
      a_win.value = store.state.record.a_win;
      a_lose.value = store.state.record.a_lose;
      a_draw.value = store.state.record.a_draw;

      b_username.value = store.state.record.b_username;
      b_photo.value = store.state.record.b_photo;
      b_win.value = store.state.record.b_win;
      b_lose.value = store.state.record.b_lose;
      b_draw.value = store.state.record.b_draw;
    } else {
      const userId = parseInt(store.state.user.id);
      const aId = store.state.battle.a_id;
      const bId = store.state.battle.b_id;
      if (userId === aId) {
        a_username.value = store.state.user.username;
        a_photo.value = store.state.user.photo;
        a_win.value = store.state.user.win;
        a_lose.value = store.state.user.lose;
        a_draw.value = store.state.user.draw;

        b_username.value = store.state.battle.opponent_username;
        b_photo.value = store.state.battle.opponent_photo;
        b_win.value = store.state.battle.opponent_win;
        b_lose.value = store.state.battle.opponent_lose;
        b_draw.value = store.state.battle.opponent_draw;
      } else if (userId === bId) {
        a_username.value = store.state.battle.opponent_username;
        a_photo.value = store.state.battle.opponent_photo;
        a_win.value = store.state.battle.opponent_win;
        a_lose.value = store.state.battle.opponent_lose;
        a_draw.value = store.state.battle.opponent_draw;

        b_username.value = store.state.user.username;
        b_photo.value = store.state.user.photo;
        b_win.value = store.state.user.win;
        b_lose.value = store.state.user.lose;
        b_draw.value = store.state.user.draw;
      }
    }
    return {
      a_username,
      a_photo,
      a_win,
      a_lose,
      a_draw,
      b_username,
      b_photo,
      b_win,
      b_lose,
      b_draw,
    };
  },
};
</script>

<style scoped>
div.playground {
  width: 95%;
  height: 95%;
  margin: 20px auto;
}
div.playground-username {
  margin-top: 2vh;
  font-size: 20px;
  font-weight: 600;
  text-align: center;
}
div.user-history {
  text-align: center;
  font-size: 16px;
  font-weight: 600;
  padding-top: 2vh;
}
</style>
