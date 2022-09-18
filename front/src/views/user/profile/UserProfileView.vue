<template>
  <div class="container">
    <div class="row justify-content-md-center">
      <div class="col-3">
        <div class="card" style="margin-top: 20px">
          <div class="card-body">
            <img :src="userdetail.photo" alt="" style="width: 100%" />
          </div>
        </div>
      </div>
      <ContentField class="col-9">
        <form>
          <div class="mb-3">
            <label for="firstName" class="form-label">First Name</label>
            <input
              v-model="userdetail.firstName"
              type="text"
              class="form-control"
              id="firstName"
              disabled
            />
          </div>
          <div class="mb-3">
            <label for="lastName" class="form-label">Last Name</label>
            <input
              v-model="userdetail.lastName"
              type="text"
              class="form-control"
              id="lastName"
              disabled
            />
          </div>
          <div class="mb-3">
            <label for="username" class="form-label">Username</label>
            <input
              v-model="userdetail.username"
              type="text"
              class="form-control"
              id="username"
              disabled
            />
          </div>
          <div class="mb-3">
            <label for="emailAddress" class="form-label">Email</label>
            <input
              v-model="userdetail.emailAddress"
              type="text"
              class="form-control"
              id="emailAddress"
              disabled
            />
          </div>
        </form>
      </ContentField>
    </div>
  </div>
</template>

<script>
import { useStore } from "vuex";
import $ from "jquery";
import { ref } from "vue";
import ContentField from "../../../components/ContentField.vue";

export default {
  components: {
    ContentField,
  },
  setup() {
    const store = useStore();
    let userdetail = ref([]);

    const refresh_user = () => {
      $.ajax({
        url: "http://127.0.0.1:3000/user/account/info/",
        type: "get",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(res) {
          userdetail.value = res;
        },
      });
    };

    refresh_user();

    return {
      userdetail,
    };
  },
};
</script>
<style scoped></style>
