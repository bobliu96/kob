<template>
  <ContentField>
    <table class="table table-striped table-hover" style="text-align: center">
      <thead>
        <tr>
          <th>Player Name</th>
          <th>Rating</th>
          <th>Win</th>
          <th>Lose</th>
          <th>Draw</th>
          <th>Win Rate</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.id">
          <td>
            <img :src="user.photo" alt="" class="user-photo" />
            <!-- space -->
            &nbsp;
            <span class="user-username" v-if="user.username === $store.state.user.username" style="color:red;font-weight: bold;">{{ user.username }}</span>
            <span class="user-username" v-else >{{ user.username }}</span>
          </td>
          <td v-if="user.username === $store.state.user.username" style="color:red;font-weight: bold;">{{ user.rating }}</td>
          <td v-else>{{ user.rating }}</td>
          <td >{{ user.win }}</td>
          <td >{{ user.lose }}</td>
          <td >{{ user.draw }}</td>
          <td v-if="user.win + user.lose + user.draw === 0">{{ 0 }}</td>
          <td v-else>{{ (user.win/(user.win + user.lose + user.draw)).toFixed(3) }}</td>
        </tr>
      </tbody>
    </table>

    <nav aria-label="...">
      <ul class="pagination" style="float: right">
        <li class="page-item" @click="click_page(-2)">
          <a class="page-link" href="#">Previous</a>
        </li>
        <li
          :class="'page-item ' + page.is_active"
          v-for="page in pages"
          :key="page.number"
          @click="click_page(page.number)"
        >
          <a class="page-link" href="#">{{ page.number }}</a>
        </li>
        <li class="page-item" @click="click_page(-1)">
          <a class="page-link" href="#">Next</a>
        </li>
      </ul>
    </nav>
  </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue";
import { useStore } from "vuex";
import { ref } from "vue";
import $ from "jquery";
export default {
  components: {
    ContentField,
  },
  setup() {
    const store = useStore();
    let users = ref([]);
    let current_page = 1;
    let total_users = 0;
    let pages = ref([]);
    const click_page = (page) => {
      if (page === -2) {
        page = current_page - 1;
      } else if (page === -1) {
        page = current_page + 1;
      }
      let max_pages = parseInt(Math.ceil(total_users / 3));
      if (page >= 1 && page <= max_pages) {
        pull_page(page);
      }
    };
    const update_pages = () => {
      let max_pages = parseInt(Math.ceil(total_users / 3));
      let new_pages = [];
      for (let i = current_page - 2; i <= current_page + 2; i++) {
        if (i >= 1 && i <= max_pages) {
          new_pages.push({
            number: i,
            is_active: i === current_page ? "active" : "",
          });
        }
      }
      pages.value = new_pages;
    };
    const pull_page = (page) => {
      current_page = page;
      $.ajax({
        url: "http://127.0.0.1:3000/ranklist/getlist/",
        type: "GET",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        data: {
          page,
        },
        success(resp) {
          users.value = resp.users;
          total_users = resp.users_count;
          update_pages();
        },
        error(err) {
          console.log(err);
        },
      });
    };
    pull_page(current_page);
    return { users, pages, click_page };
  },
};
</script>

<style scoped>
img.user-photo {
  width: 4vh;
  border-radius: 50%;
}
</style>