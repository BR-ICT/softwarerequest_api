import React, { useEffect, useState, useMemo, useRef } from "react";
import { useSelector, useDispatch } from "react-redux";
import moment from "moment";
import MaterialTable, { MTableToolbar } from "material-table";
import * as monitoringreceiptActions from "../../../actions/monitoringreceipt.action";
import IconButton from "@material-ui/core/IconButton";
import { Save, Delete } from "@material-ui/icons";
import axios from "axios";

import ScreenshotIcon from "@mui/icons-material/Screenshot";

import { CSSTransition, TransitionGroup } from "react-transition-group";

import { makeStyles } from "@material-ui/core/styles";
import { Button, Typography, Container, Box } from "@material-ui/core";
import { CheckCircleOutline } from "@material-ui/icons";
import CircularProgress from "@mui/material/CircularProgress";

import { createTheme, ThemeProvider } from "@material-ui/core/styles";
import { Switch, Route, useLocation } from "react-router-dom";

import html2canvas from "html2canvas";

import {
  red,
  green,
  purple,
  teal,
  deepOrange,
  blueGrey,
  yellow,
} from "@material-ui/core/colors/";

import { withStyles, createMuiTheme } from "@material-ui/core/styles";
import { size } from "lodash";

const theme = createTheme({
  palette: {
    primary: {
      main: "#1976d2",
      light: "#42a5f5",
      dark: "#1565c0",
      contrastText: "#242105",
    },
    aa: {
      main: "#ed6c02",
      light: "#ff9800",
      dark: "#e65100",
      contrastText: "#242105",
    },
    secondary: {
      main: "#d32f2f",
      light: "#ef5350",
      dark: "#c62828",
      contrastText: "#242105",
    },
    default: {
      main: "#2e7d32",
      light: "#4caf50",
      dark: "#1b5e20",
      contrastText: "#242105",
    },
  },
});

const useStyles = makeStyles((theme) => ({
  root: {
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    height: "100vh",
    backgroundColor: theme.palette.background.default,
  },
  container: {
    textAlign: "center",
    backgroundColor: theme.palette.background.paper,
    padding: theme.spacing(6),
    borderRadius: theme.shape.borderRadius,
    boxShadow: theme.shadows[5],
  },
  icon: {
    color: theme.palette.success.main,
    fontSize: 64,
    marginBottom: theme.spacing(2),
  },
  button: {
    marginTop: theme.spacing(4),
  },
}));
// const useStyles = makeStyles((theme) => ({
//   root: {
//     width: "100%",
//     marginTop: 60,
//   },
//   paper: {
//     padding: theme.spacing(2),
//     color: theme.palette.text.secondary,
//   },
//   margin: {
//     marginTop: "0.4rem",
//     marginRight: "0.4rem",
//     margin: theme.spacing(0.3),
//   },
//   extendedIcon: {
//     marginRight: theme.spacing(1),
//   },
// }));

export default (props) => {
  const classes = useStyles();
  const dispatch = useDispatch();
  const monitoringreceiptReducer = useSelector(
    ({ monitoringreceiptReducer }) => monitoringreceiptReducer
  );

  const [idhead, setidhead] = useState("24000000");

  const [loading, setLoading] = useState(false);

  // Zimbra Zone
  // --------------------------------------------------

  // const [events, setEvents] = useState([]);
  // const [loadingz, setLoadingz] = useState(true);

  // useEffect(() => {
  //   const fetchCalendarData = async () => {
  //     try {
  //       const response = await axios.get(
  //         // "https://your-zimbra-server/service/home/user@domain.com/calendar.json",
  //         "https://192.200.9.174/service/home/phongsathon@br-bangkokranch.com/calendar.json",
  //         {
  //           auth: {
  //             username: "phongsathon@br-bangkokranch.com",
  //             password: "Aoy59264866",
  //           },
  //         }
  //       );
  //       setEvents(response.data);
  //       setLoadingz(false);
  //     } catch (error) {
  //       console.error("Error fetching calendar data:", error);
  //       setLoadingz(false);
  //     }
  //   };
  //   fetchCalendarData();
  // }, []);

  // --------------------------------------------------
  const initialsuccessheader = {
    vID: "24000000",
    vTYPE: "SUBMIT",
  };

  const [successheader, setsuccessheader] = useState(initialsuccessheader);
  const location = useLocation();

  useEffect(() => {
    let params = props.match.params;
    setsuccessheader({
      ...successheader,
      vID: params.id,
      vTYPE: params.type,
    });

    setidhead(params.id);
  }, []);

  useEffect(() => {
    // const timer = setTimeout(() => {
    //   window.location.href = "https://www.bangkokranch.com/products/";
    // }, 3000);
  });

  const screenshotRef = useRef(null);

  const handleScreenshot = () => {
    const element = screenshotRef.current;
    html2canvas(element).then((canvas) => {
      const link = document.createElement("a");
      link.href = canvas.toDataURL("image/png");
      link.download = "screenshot.png";
      link.click();
    });
  };

  return (
    <TransitionGroup>
      <br />
      <br />
      <br />
      <br />

      {/* <div>
        <h1>Zimbra Calendar Events</h1>
        <ul>
          {events.map((event, index) => (
            <li key={index}>
              <strong>{event.name}</strong> - {event.startDate} to{" "}
              {event.endDate}
            </li>
          ))}
        </ul>
      </div> */}
      {/* <div>
        <h1>Zimbra Calendar Events</h1>
        <ul>
          {events.map((event, index) => (
            <li key={index}>
              <strong>{event.name}</strong> - {event.startDate} to{" "}
              {event.endDate}
            </li>
          ))}
        </ul>
      </div> */}
      <div>
        <div
          ref={screenshotRef}
          style={{ padding: "20px", border: "1px solid #ccc" }}
        >
          {loading && (
            <div
              style={{
                position: "fixed",
                top: "50%",
                left: "50%",
                transform: "translate(-50%, -50%)",
                zIndex: 2,
              }}
            >
              <CircularProgress style={{ color: "black" }} />
            </div>
          )}
          <CSSTransition key={location.key} timeout={300} classNames="fade">
            <div className={classes.root}>
              <Container className={classes.container} maxWidth="sm">
                <CheckCircleOutline className={classes.icon} />
                <Typography variant="h5" component="h3" gutterBottom>
                  {successheader.vTYPE} Form Done!
                </Typography>
                <Typography
                  sx={{
                    top: "104px",
                    fontFamily: "Roboto",
                    fontStyle: "normal",
                    fontWeight: "normal",
                    lineHeight: "24px",
                    fontSize: "16px",
                    letterSpacing: "0.18px",
                    color: "#172B4D",
                    margin: "16px 0px",
                  }}
                >
                  Your ID :
                  <Typography
                    sx={{ textDecoration: "underline" }}
                    display="inline"
                  >
                    {idhead}
                  </Typography>
                </Typography>
                <Typography variant="body1" gutterBottom>
                  Thank you for completing your visitor form.
                </Typography>
                <Typography variant="body2">
                  Please Show ID to Operator.
                </Typography>
                <Box className={classes.button}>
                  <Button
                    variant="contained"
                    color="primary"
                    onClick={async () => {
                      // props.history.goBack();
                      // props.history.push("https://www.bangkokranch.com/products/");
                      window.location.href =
                        "https://www.bangkokranch.com/products/";
                    }}
                  >
                    Enter the BR Group Site.
                  </Button>
                </Box>
              </Container>
            </div>
          </CSSTransition>
        </div>

        <Button
          style={{
            width: "150px",
            height: "50px",
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            justifyContent: "center",
            textAlign: "center",
            padding: "20px",
            backgroundColor: "transparent",
            border: "none",
          }}
          variant="contained"
          size="large"
          onClick={async () => {
            handleScreenshot();
          }}
        >
          <ScreenshotIcon style={{ fontSize: "50px", color: "black" }} />
          Take Screenshot
        </Button>
      </div>
    </TransitionGroup>
  );
};
