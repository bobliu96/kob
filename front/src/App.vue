<template>
  <NavBar />
  <router-view />
</template>

<script>
import NavBar from "./components/NavBar.vue";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap";

export default {
  components: {
    NavBar,
  },
//   setup() {
//     $.ajax({
//       url: "http://127.0.0.1:3000/user/account/token/",
//       type: "post",
//       data: {
//         username: "bo",
//         password: "sssss",
//       },
//       success(res) {
//         console.log(res);
//       },
//       error(res) {
//         console.log(res);
//       },
//     });

//     $.ajax({
//       url: "http://127.0.0.1:3000/user/account/info/",
//       type: "get",
//       headers: {
//         Authorization:
//           "Bearer" +
//           "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJmMTVjOTVmMjYwMGM0OTJhYjJhOGFmM2Y2M2I5MWFkZiIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY2MzM2NjgzNywiZXhwIjoxNjY0NTc2NDM3fQ.CzNwnSg-CncUUVHjoDoxtrXmQjPo_ikrJ7ZPNPdB9J4",
//       },
//       success(res) {
//         console.log(res);
//       },
//       error(res) {
//         console.log(res);
//       },
//     });

//     $.ajax({
//       url: "http://127.0.0.1:3000/user/account/register/",
//       type: "post",
//       data: {
//         username: "bol",
//         password: "sssss",
//         confirmedPassword: "sssss",
//       },
//       success(res) {
//         console.log(res);
//       },
//       error(res) {
//         console.log(res);
//       },
//     });
//   },
};
</script>

<style>
body {
  background-image: url("@/assets/images/background.png");
  background-size: cover;
}
</style>
