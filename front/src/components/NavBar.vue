<template>
  <nav class="navbar navbar-expand-lg bg-light">
    <div class="container">
      <router-link class="navbar-brand" :to="{ name: 'home' }"
        >King of Bots</router-link
      >
      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarText"
        aria-controls="navbarText"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link
              :class="route_name == 'battle_index' ? 'nav-link active' : 'nav-link'"
              :to="{ name: 'battle_index' }"
              >Start Battle!</router-link
            >
          </li>
          <li class="nav-item">
            <router-link
              :class="route_name == 'record_index' ? 'nav-link active' : 'nav-link'"
              :to="{ name: 'record_index' }"
              >Match Record</router-link
            >
          </li>
          <li class="nav-item">
            <router-link
              :class="route_name == 'ranking_index' ? 'nav-link active' : 'nav-link'"
              :to="{ name: 'ranking_index' }"
              >Rank List</router-link
            >
          </li>
        </ul>
        <ul class="navbar-nav" v-if="$store.state.user.is_login">
          <li class="nav-item dropdown">
            <a
              class="nav-link dropdown-toggle"
              href="#"
              role="button"
              data-bs-toggle="dropdown"
              aria-expanded="false"
            >
              {{ $store.state.user.username }}
            </a>
            <ul class="dropdown-menu">
              <li>
                <router-link
                  class="dropdown-item"
                  :to="{ name: 'user_bot_index' }"
                  >My Bots</router-link
                >
              </li>
              <li>
                <router-link
                  class="dropdown-item"
                  :to="{ name: 'user_profile' }"
                  >Profile</router-link
                >
              </li>
              <li>
                <a class="dropdown-item" href="#" @click="logout">Logout</a>
              </li>
            </ul>
          </li>
        </ul>
        <ul class="navbar-nav" v-else>
          <li class="nav-item dropdown">
            <a
              class="nav-link dropdown-toggle"
              href="#"
              role="button"
              data-bs-toggle="dropdown"
              aria-expanded="false"
            >
              Login/Register
            </a>
            <ul class="dropdown-menu">
              <li>
                <router-link
                  class="dropdown-item"
                  :to="{ name: 'user_account_login' }"
                  >login</router-link
                >
              </li>
              <li>
                <router-link class="dropdown-item" :to="{ name: 'user_account_register' }"
                  >Register</router-link
                >
              </li>
            </ul>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
import { useRoute } from "vue-router";
import { computed } from "vue";
import { useStore } from "vuex";

export default {
  setup() {
    const store = useStore();
    const route = useRoute();
    let route_name = computed(() => route.name);
    const logout = () => {
      store.dispatch("logout");
    };
    return {
      route_name,
      logout,
    };
  },
};
</script>

<style scoped></style>
