import React, { useEffect, useState, useMemo } from "react";
import { useSelector, useDispatch } from "react-redux";
import logo from "./logo.svg";
import "./App.css";
import { makeStyles, useTheme } from "@material-ui/core/styles";
import { createMuiTheme, ThemeProvider } from "@material-ui/core/styles";
import {
  BrowserRouter as Router,
  Route,
  Redirect,
  Switch,
} from "react-router-dom";
import Container from "@material-ui/core/Container";
import Drawer from "./components/layouts/Drawer";
import PublicDrawer from "./components/layouts/PublicDrawer";
import "./index.css"; // Import the global CSS file

import * as loginActions from "./actions/login.action";
import LoginPage from "./components/pages/LoginPage/LoginPage";
import HomePage from "./components/pages/HomePage/HomePage";
import PlanMARPage from "./components/pages/PlanMARPage/PlanMARPage";
import EditOrderPage from "./components/pages/EditOrderPage/EditOrderPage";
import ConfirmMARPage from "./components/pages/ConfirmMARPage/ConfirmMARPage";
import PrintMARPage from "./components/pages/PrintMARPage/PrintMARPage";
import ConfirmM3Page from "./components/pages/ConfirmM3Page/ConfirmM3Page";
import MonitoringPage from "./components/pages/MonitoringPage/MonitoringPage";
import DeptAndCostPage from "./components/pages/DeptAndCostPage/DeptAndCostPage";
import TestPage from "./components/pages/TestPage/TestPage";
import MonitoringReceiptPage from "./components/pages/MonitoringReceiptPage";
import Filepage from "./components/pages/FilePage";
import Main_Requestpage from "./components/pages/Main_Request";
import NewSWRpage from "./components/pages/NewSWRPage";
import VisitorPage from "./components/pages/VisitorPage";
import OperatorPage from "./components/pages/OperatorPage";
import ShowVisitorPage from "./components/pages/ShowVisitorPage";
import SuccessPage from "./components/pages/SuccessPage";
import PlanPage from "./components/pages/PlanPage";
import UploadPage from "./components/pages/UploadPage";
import CreateIDPage from "./components/pages/CreateIDPage";
import ReturnPage from "./components/pages/ReturnPage";

// import SuccessPage from "./components/pages/SuccessPage";

const useStyles = makeStyles((theme) => ({
  root: {
    display: "flex",
  },
  toolbar: {
    display: "flex",
    alignItems: "center",
    justifyContent: "flex-end",
    padding: theme.spacing(0, 1),
    ...theme.mixins.toolbar,
  },
  content: {
    flexGrow: 1,
    display: "flex",
    justifyContent: "center",
    padding: theme.spacing(3),
  },
}));

export default function App() {
  const classes = useStyles();

  // Login Route
  const LoginRoute = ({ component: Component, ...rest }) => (
    <Route
      {...rest}
      render={(props) =>
        loginActions.checkLoginAuthen("login", props.history) ? (
          <Redirect to={{ pathname: "/", state: { from: props.location } }} />
        ) : (
          <div className={classes.root}>
            <Container className={classes.content} maxWidth={false}>
              <Component {...props} />
            </Container>
          </div>
        )
      }
    />
  );

  //Private Route
  const PrivateRoute = ({ component: Component, ...rest }) => (
    <Route
      {...rest}
      render={(props) =>
        loginActions.checkLoginAuthen("private", props.history) ? (
          <div className={classes.root}>
            <Drawer company={loginActions.getTokenCompany()} />
            <Container className={classes.content} maxWidth={false}>
              <Component {...props} />
            </Container>
          </div>
        ) : (
          <Redirect
            to={{ pathname: "/login", state: { from: props.location } }}
          />
        )
      }
    />
  );

  //Public Route
  const PublicRoute = ({ component: Component, ...rest }) => (
    <Route
      {...rest}
      render={
        (props) => (
          // loginActions.isLoggedIn() ? (
          <div className={classes.root}>
            <PublicDrawer />
            <Container className={classes.content} maxWidth={false}>
              <Component {...props} />
            </Container>
          </div>
        )
        // ) : (
        //   <Redirect
        //     to={{ pathname: "/login", state: { from: props.location } }}
        //   />
        // )
      }
    />
  );

  return (
    <Router
      basename={
        process.env.REACT_APP_IS_PRODUCTION === "1" ? "/bankmapping" : ""
      }
    >
      <Switch>
        <LoginRoute exact path="/login" component={LoginPage} />
        <PrivateRoute exact path="/" component={HomePage} />
        <PrivateRoute exact path="/plan_mar" component={PlanMARPage} />
        <PrivateRoute exact path="/editorder" component={EditOrderPage} />
        <PrivateRoute exact path="/confirm_mar" component={ConfirmMARPage} />
        <PrivateRoute exact path="/print_mar" component={PrintMARPage} />
        <PrivateRoute exact path="/confirmm3" component={ConfirmM3Page} />
        <PrivateRoute exact path="/monitoring" component={MonitoringPage} />
        <PrivateRoute exact path="/deptandcost" component={DeptAndCostPage} />
        <PrivateRoute exact path="/file" component={Filepage} />
        <PrivateRoute exact path="/newswr" component={NewSWRpage} />
        {/* <PublicRoute exact path="/visitor" component={VisitorPage} /> */}
        <PrivateRoute exact path="/UploadPage" component={UploadPage} />
        <PrivateRoute exact path="/CreateIDPage" component={CreateIDPage} />
        <PrivateRoute exact path="/ReturnPage" component={ReturnPage} />

        <PrivateRoute exact path="/visit/:cono" component={VisitorPage} />

        <PublicRoute
          exact
          path="/successpage/:id/:type"
          component={SuccessPage}
        />

        {/* <PrivateRoute
          exact
          path="/visit/:cono/:divi/:location"
          component={VisitorPage}
        /> */}

        <PublicRoute
          exact
          path="/visit/:cono/:divi/:location"
          component={VisitorPage}
        />

        <PublicRoute
          exact
          path="/showvisitor/:location/:id"
          component={ShowVisitorPage}
        />

        <PrivateRoute exact path="/operator" component={OperatorPage} />

        <PrivateRoute exact path="/PlanPage" component={PlanPage} />

        <PrivateRoute exact path="/main_request" component={Main_Requestpage} />
        <PrivateRoute
          exact
          path="/monitoringreceipt"
          component={MonitoringReceiptPage}
        />
        {/* The Default not found component */}
        {/* <Route render={(props) => <Redirect to="/" />} /> */}
      </Switch>
    </Router>
  );
}
