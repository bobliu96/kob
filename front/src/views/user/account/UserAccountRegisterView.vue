<template>
  <ContentField>
    <div class="row justify-content-md-center">
      <div class="col-3">
        <form @submit.prevent="register">
          <div class="mb-3">
            <label for="firstName" class="form-label">First Name</label>
            <input
              v-model="firstName"
              type="text"
              class="form-control"
              id="firstName"
              placeholder="first name"
            />
          </div>
          <div class="mb-3">
            <label for="lastName" class="form-label">Last Name</label>
            <input
              v-model="lastName"
              type="text"
              class="form-control"
              id="lastName"
              placeholder="last name"
            />
          </div>
          <div class="mb-3">
            <label for="username" class="form-label">Username</label>
            <input
              v-model="username"
              type="text"
              class="form-control"
              id="username"
              placeholder="username"
            />
          </div>
          <div class="mb-3">
            <label for="password" class="form-label">Password</label>
            <input
              v-model="password"
              type="password"
              class="form-control"
              id="password"
              placeholder="password"
            />
          </div>
          <div class="mb-3">
            <label for="confirmedPassword" class="form-label">Confirm Password</label>
            <input
              v-model="confirmedPassword"
              type="password"
              class="form-control"
              id="confirmedPassword"
              placeholder="confirm password"
            />
          </div>
          <div class="mb-3">
            <label for="emailAddress" class="form-label">Email</label>
            <input
              v-model="emailAddress"
              type="text"
              class="form-control"
              id="emailAddress"
              placeholder="Email Address"
            />
          </div>
          <div class="error-message">{{ error_message }}</div>
          <button type="submit" class="btn btn-primary">Submit</button>
        </form>
      </div>
    </div>
  </ContentField>
</template>

<script>
import ContentField from "../../../components/ContentField.vue";
import { ref } from 'vue'
import router from '../../../router/index'
import $ from "jquery";

export default {
  components: {
    ContentField,
  },
  setup() {
    let username = ref("");
    let password = ref("");
    let confirmedPassword = ref("");
    let firstName = ref("");
    let lastName = ref("");
    let emailAddress = ref("");
    let error_message = ref("");

    const register = () => {
      $.ajax({
        url: "http://127.0.0.1:3000/user/account/register/",
        type: "post",
        data: {
          username: username.value,
          password: password.value,
          confirmedPassword: confirmedPassword.value,
          firstName: firstName.value,
          lastName: lastName.value,
          emailAddress: emailAddress.value,
        },
        success: (resp) => {
          if (resp.error_message === "success") {
            alert("User registration successfully.");
            router.push({ name: "user_account_login" });
          } else {
            error_message.value = resp.error_message;
          }
        },
      });
    };

    return {
      username,
      password,
      confirmedPassword,
      firstName,
      lastName,
      emailAddress,
      error_message,
      register,
    }
  }
};
</script>

<style scoped>
button {
  width: 100%;
}
div.error-message {
  color: red;
}
</style>