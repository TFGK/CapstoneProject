import Dashboard from "views/Dashboard.jsx";
import UserProfile from "views/UserProfile.jsx";
import Maps from "views/Maps.jsx";
import Create from "views/Create.jsx";
import TableList from "views/TableList.jsx";

import Login from "views/Login.jsx";
import Register from "views/Register.jsx";

const dashboardRoutes = [
  {
    path: "/dashboard",
    name: "Dashboard",
    icon: "pe-7s-graph",
    component: Dashboard,
    layout: "/admin"
  },
  
  {
    path: "/userprofile",
    name: "UserProfile",
    icon: "pe-7s-user",
    component: UserProfile,
    layout: "/admin"
  },
  {
    path: "/maps",
    name: "Maps",
    icon: "pe-7s-map-marker",
    component: Maps,
    layout: "/admin"
  },

  //table
  {
    path: "/create",
    name: "Create",
    icon: "pe-7s-user",
    component: Create,
    layout: "/admin"
  },
  //table
  {
    path: "/table",
    name: "board",
    icon: "pe-7s-user",
    component: TableList,
    layout: "/admin"
  },

  // auth
  {
    path: "/login",
    component: Login,
    layout: "/admin"
  },
  {
    path: "/register",
    component: Register,
    layout: "/admin"
  },
];

export default dashboardRoutes;
