import { createRouter, createWebHistory } from "vue-router";
import BattleIndexView from "../views/battle/BattleIndexView";
import RecordIndexView from "../views/record/RecordIndexView";
import RankingIndexView from "../views/ranklist/RanklistIndexView";
import UserBotIndexView from "../views/user/bot/UserBotIndexView";
import NotFound from "../views/error/NotFound";

const routes = [
  {
    path: "/",
    name: "home",
    component: BattleIndexView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: "/battle/",
    name: "battle_index",
    component: BattleIndexView,
    meta: {
      requestAuth: true,
    },
  },

  {
    path: "/record/",
    name: "record_index",
    component: RecordIndexView,
    meta: {
      requestAuth: true,
    },
  },

  {
    path: "/ranking/",
    name: "ranking_index",
    component: RankingIndexView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: "/user/bot/",
    name: "user_bot_index",
    component: UserBotIndexView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: "/404/",
    name: "not_found",
    component: NotFound,
    meta: {
      requestAuth: false,
    },
  },
  { path: "/:catchAll(.*)", redirect: "/404/" },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
