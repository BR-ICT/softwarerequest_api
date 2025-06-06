import React, { useState } from "react";
import { Link, withRouter, NavLink } from "react-router-dom";
import clsx from "clsx";
import { useSelector, useDispatch } from "react-redux";
import { makeStyles, useTheme } from "@material-ui/core/styles";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import CssBaseline from "@material-ui/core/CssBaseline";
import Typography from "@material-ui/core/Typography";
import IconButton from "@material-ui/core/IconButton";
import MenuIcon from "@material-ui/icons/Menu";
import * as loginActions from "./../../actions/login.action";

const drawerWidth = 240;

const useStyles = makeStyles((theme) => ({
  root: {
    display: "flex",
  },
  appBar: {
    zIndex: theme.zIndex.drawer + 1,
    transition: theme.transitions.create(["width", "margin"], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
  },
  appBarShift: {
    marginLeft: drawerWidth,
    width: `calc(100% - ${drawerWidth}px)`,
    transition: theme.transitions.create(["width", "margin"], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  menuButton: {
    marginRight: 36,
  },
  hide: {
    display: "none",
  },
  drawer: {
    width: drawerWidth,
    flexShrink: 0,
    whiteSpace: "nowrap",
  },
  draweropendrawer: {
    width: drawerWidth,
    transition: theme.transitions.create("width", {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  drawerClose: {
    transition: theme.transitions.create("width", {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
    overflowX: "hidden",
    // width: theme.spacing(7) + 1,
    width: 0,
    [theme.breakpoints.up("sm")]: {
      // width: theme.spacing(9) + 1
      width: 0,
    },
  },
  toolbar: {
    display: "flex",
    alignItems: "center",
    justifyContent: "flex-end",
    padding: theme.spacing(0, 1),
    // necessary for content to be below app bar
    ...theme.mixins.toolbar,
  },
  content: {
    flexGrow: 1,
    padding: theme.spacing(3),
  },
  isActive: {
    backgroundColor: "#e0f5fd",
    color: "#0080ff",
  },
  sectionDesktop: {
    display: "none",
    [theme.breakpoints.up("md")]: {
      display: "flex",
    },
  },
  sectionMobile: {
    display: "flex",
    [theme.breakpoints.up("md")]: {
      display: "none",
    },
  },
  grow: {
    flexGrow: 1,
  },
}));

const MiniDrawer = (props) => {
  const classes = useStyles();
  const theme = useTheme();
  const [opendrawer, setOpenDrawer] = useState(false);
  const [openmenuph, setOpenMenuPH] = useState(false);
  const [anchorEl, setAnchorEl] = useState(null);
  const dispatch = useDispatch();
  const isMenuOpen = Boolean(anchorEl);

  const handleDraweropendrawer = () => {
    setOpenDrawer(false);
  };

  const handleDrawerClose = () => {
    setOpenDrawer(false);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleProfileMenuopendrawer = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClick = () => {
    setOpenMenuPH(!openmenuph);
  };

  const logout = () => {
    dispatch(loginActions.logout(props.history));
    props.history.push("./");
    handleMenuClose();
  };

  const menuId = "primary-search-account-menu";

  return (
    <div>
      <CssBaseline />
      <AppBar
        color={
          process.env.REACT_APP_IS_PRODUCTION === "1" ? "primary" : "secondary"
        }
        position="fixed"
        className={clsx(classes.appBar, {
          [classes.appBarShift]: opendrawer,
        })}
      >
        <Toolbar>
          <IconButton
            color="inherit"
            aria-label="opendrawer drawer"
            onClick={handleDraweropendrawer}
            edge="start"
            className={clsx(classes.menuButton, {
              [classes.hide]: opendrawer,
            })}
          >
            <MenuIcon />
          </IconButton>

          <Typography variant="h6" noWrap>
            Smart Purchase : Monthly Plan - Ver {process.env.REACT_APP_VERSION}
            {/* <Typography variant="body1"> Approve </Typography> */}
            <Typography variant="body1">{props.company}</Typography>
          </Typography>

          <div className={classes.grow} />
        </Toolbar>
      </AppBar>
    </div>
  );
};

export default withRouter(MiniDrawer);
