import React, { useEffect, useState } from "react";
import { Formik, Form, Field } from "formik";
import MaterialTable, { MTableToolbar } from "material-table";
import { useSelector, useDispatch } from "react-redux";
import Select, { SelectChangeEvent } from "@mui/material/Select";
import MenuItem from "@mui/material/MenuItem";
import Box from "@mui/material/Box";
import SplitPane from "react-split-pane";
import Autocomplete from "@mui/material/Autocomplete";
import Modal from "@mui/material/Modal";
import AddIcon from "@material-ui/icons/Add";
import InfoIcon from "@mui/icons-material/Info";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import EmailIconsend from "@mui/icons-material/MarkEmailRead";
import EmailIcon from "@mui/icons-material/Email";
import SearchIcon from "@mui/icons-material/Search";
import CloseIcon from "@mui/icons-material/Close";

// import { Modal } from "react-responsive-modal";

import * as CheckoutActions from "../../../actions/checkout.action";

import {
  Typography,
  Grid,
  Paper,
  TextField,
  Button,
  Input,
  colors,
} from "@material-ui/core";
import {
  makeStyles,
  withStyles,
  ThemeProvider,
  createMuiTheme,
  createTheme,
} from "@material-ui/core/styles";
import {
  red,
  green,
  purple,
  teal,
  deepOrange,
  blueGrey,
  yellow,
} from "@material-ui/core/colors/";

import * as sendmailActionspp from "../../../actions/sendemailpp.action";

import * as swrfileActions from "../../../actions/swrfile.action";

import * as roomcardActions from "../../../actions/roomcard.action";

import * as swrIDActions from "../../../actions/swrID.action";

import * as operationdataActions from "../../../actions/operatordata.action";

import * as getimageActions from "../../../actions/getimage.action";

import * as loginActions from "../../../actions/login.action";

import * as headerActions from "../../../actions/swrheader.action";

import * as devActions from "../../../actions/swrdev.action";

import * as swrfilenumberActions from "../../../actions/swrfilenumber.action";
import { set } from "lodash";
import { IconButton } from "@mui/material";
import { FreeBreakfast } from "@material-ui/icons";

import * as employeeActions from "../../../actions/employee.action";

import * as followerActions from "../../../actions/follower.action";
import followerReducer from "../../../reducers/follower.reducer";

// import { createTheme } from "@mui/material/styles";

// import swrfileReducer from "../../../reducers/swrfile.reducer";

// import swrfilenumberReducer from "../../../reducers/swrfilenumber.reducer";

// const employeeReducer = useSelector(({ employeeReducer }) => employeeReducer);

// const employeeset = employeeReducer.result ? employeeReducer.result : [];

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
    width: "100%",
    marginTop: 60,
  },
  paper: {
    padding: theme.spacing(2),
    color: theme.palette.text.secondary,
  },
  margin: {
    marginTop: "0.4rem",
    marginRight: "0.4rem",
    margin: theme.spacing(0.3),
  },
  extendedIcon: {
    marginRight: theme.spacing(1),
  },
  row: {
    borderLeft: 1,
    borderRight: 1,
    borderBottom: 1,
    borderTop: 1,
    borderColor: "#E0E0E0",
    borderStyle: "solid",
  },
  wrapper: {
    margin: "3px",
    position: "relative",
  },
  buttonSuccess: {
    backgroundColor: green[500],
    "&:hover": {
      backgroundColor: green[700],
    },
  },
  fabProgress: {
    color: green[500],
    position: "absolute",
    top: -6,
    left: -6,
    zIndex: 1,
  },
  buttonProgress: {
    color: green[500],
    position: "absolute",
    top: "50%",
    left: "50%",
    marginTop: -12,
    marginLeft: -12,
  },
}));

const FilePage = (props) => {
  const initialFileDetail = {
    vReference: "aa",
    vOrderno: "bb",
    vName: "cc",
    vLine: "dd",
    vType: "ee",
    vRemark1: "ff",
    vRemark2: "gg",
  };

  const initialswrnumber = {
    vReference: "",
    vName: "",
    vLine: "",
    vType: "",
    vRemark1: "",
    vRemark2: "",
  };

  const initialheader = {
    vID: "",
    vSwrname: "",
    vVersion: "",
    vSwrtype: "",
    vReqdate: "",
    vFinishdate: "",
    vRemark: "",
    vRequester: loginActions.getTokenUsername(),
    vDepthead: "",
    vDev: "",
    vAppdevmanager: "",
    vGM: "PHAISA_KUM",
    vCIO: "WUTINA_ULI",
  };

  const initialsign = {
    vSigndate: "dd/mm/yyyy",
  };

  const initialswdetail = {
    vDetailtype: "",
    vPurpose: "",
    vScope: "",
    vDescription: "",
    vPlan: "",
    vDetail: "",
    vCondition: "",
    vPurpose: "",
  };

  //handletextfield

  const operationdataReducer = useSelector(
    ({ operationdataReducer }) => operationdataReducer
  );

  console.log(JSON.stringify(operationdataReducer.result));

  const [id, setID] = useState(false);
  const [type, setType] = useState(false);
  const [swrname, setSwrname] = useState(false);
  const [version, setVersion] = useState(false);
  const [reqdate, setReqdate] = useState(false);
  const [findate, setFindate] = useState(false);
  const [remark, setRemark] = useState(false);
  const [depthead, setDepthead] = useState(false);
  const [developer, setDeveloper] = useState(false);
  const [appdevmanager, setAppdevmaneger] = useState(false);
  const [gm, setGM] = useState(false);
  const [cio, setCIO] = useState(false);

  const [loadtable, setLoadtable] = useState(false);

  //////////////////

  //handlebutton

  const [update1, setUpdate1] = useState(false);
  const [checkin, setCheckin] = useState(false);
  const [create, setCreate] = useState(false);
  const [sendemail, setSendemail] = useState(true);
  const [clear, setClear] = useState(false);
  const [save, setSave] = useState(false);
  const [update, setUpdate] = useState(false);
  const [confirm, setConfirm] = useState(false);
  const [reject, setReject] = useState(false);
  const [editdisable, setEditDisable] = useState(false);
  const [submit, setSubmit] = useState(false);

  const dateNow = new Date(); // Creating a new date object with the current date and time
  const year = dateNow.getFullYear(); // Getting current year from the created Date object
  const monthWithOffset = dateNow.getUTCMonth() + 1; // January is 0 by default in JS. Offsetting +1 to fix date for calendar.
  const month = // Setting current Month number from current Date object
    monthWithOffset.toString().length < 2 // Checking if month is < 10 and pre-prending 0 to adjust for date input.
      ? `0${monthWithOffset}`
      : monthWithOffset;
  const date =
    dateNow.getUTCDate().toString().length < 2 // Checking if date is < 10 and pre-prending 0 if not to adjust for date input.
      ? `0${dateNow.getUTCDate()}`
      : dateNow.getUTCDate();

  const materialDateInput = `${year}-${month}-${date}`; // combining to format for defaultValue or value attribute of material <TextField>

  const [fromdate, setFromdate] = useState(materialDateInput);
  const [todate, setTodate] = useState(materialDateInput);

  const employeeReducer = useSelector(({ employeeReducer }) => employeeReducer);

  const employeeset = employeeReducer.result ? employeeReducer.result : [];

  const getimageReducer = useSelector(({ getimageReducer }) => getimageReducer);

  const getimageset = getimageReducer.result ? getimageReducer.result : [];

  const followerReducer = useSelector(({ followerReducer }) => followerReducer);

  useEffect(() => {
    dispatch(employeeActions.getEmployee());
  }, []);

  //handelfunction

  // const handleClear = () => {
  //   setADRNumber({ ...adrnumber, vADRSelectNumber: "" });
  //   setADRHead({ ...initialStateADRHead });
  //   setItemADRDetail({ ...initialStateItemADRDetail });
  //   adrheadReducer.result = null;
  //   adrdetailReducer.result = null;

  //   setCreate(true);
  //   setSearchDisable(false);
  //   setNewDisable(false);
  //   setSaveDisable(true);
  //   setClearDisable(true);
  //   setCancelDisable(true);
  //   setRejectDisable(true);
  //   setSubmitDisable(true);
  //   setEditDisable(true);
  //   setAddItemDisable(true);
  // };
  const handleSendemail = () => {
    setSendemail(true);
  };

  const handleCreate = () => {
    setSave(true);
    setClear(true);
    setCreate(false);
    setSubmit(true);
    setEditDisable(false);

    setID(true);
    setType(true);
    setSwrname(true);
    setVersion(true);
    setReqdate(true);
    setFindate(true);
    setRemark(true);
    setDepthead(true);
    setDeveloper(true);
    setAppdevmaneger(true);
    setGM(true);
    setCIO(true);

    // setADRNumber({ ...adrnumber, vADRSelectNumber: "" });
    // setADRHead({ ...initialStateADRHead });
    // setItemADRDetail({ ...initialStateItemADRDetail });
    // adrheadReducer.result = null;
    // adrdetailReducer.result = null;
    // setCreate(true);
    // setSearchDisable(false);
    // setNewDisable(false);
    // setSaveDisable(true);
    // setClearDisable(true);
    // setCancelDisable(true);
    // setRejectDisable(true);
    // setSubmitDisable(true);
    // setEditDisable(true);
    // setAddItemDisable(true);
  };

  const handleCheckin = () => {
    setCheckin(true);
  };

  const handleUpdate1 = () => {
    setUpdate1(true);
  };

  const handleUpdate1no = () => {
    setUpdate1(false);
  };

  const handleCheckinno = () => {
    setCheckin(false);
  };

  const handleClear = () => {
    setSave(false);
    setClear(false);
    setCreate(true);
    setSubmit(false);
    setEditDisable(false);

    setID(false);
    setType(false);
    setSwrname(false);
    setVersion(false);
    setReqdate(false);
    setFindate(false);
    setRemark(false);
    setDepthead(false);
    setDeveloper(false);
    setAppdevmaneger(false);
    setGM(false);
    setCIO(false);
  };
  ////////////////

  const [swrheader, setswrheader] = useState(initialheader);

  const dispatch = useDispatch();
  const classes = useStyles();

  let [imgSrc, setImgSrc] = useState("");

  useEffect(() => {
    dispatch(operationdataActions.getOperationfilterdata(fromdate, todate));
  }, []);

  console.log(operationdataReducer.result);

  const operations = [{ ID: "1111111111" }];

  const columnsOperator = [
    {
      headerStyle: {
        maxWidth: 30,
        whiteSpace: "nowrap",
        textAlign: "center",
        backgroundColor: "#e81e26",
      },
      cellStyle: {
        textAlign: "center",
        borderLeft: 1,
        borderRight: 1,
        borderBottom: 1,
        borderTop: 1,
        borderColor: "#E0E0E0",
        borderStyle: "solid",
        paddingLeft: "6px",
        paddingRight: "6px",
        paddingBottom: "12px",
        paddingTop: "12px",
        editable: "never",
      },
      render: (rowData) =>
        rowData.NAME !== "Follower" && (
          <Button
            variant="contained"
            startIcon={
              rowData.STATUS == "10" ? (
                <InfoIcon />
              ) : rowData.STATUS == "15" ? (
                <InfoIcon style={{ color: green[500] }} />
              ) : (
                <InfoIcon style={{ color: green[500] }} />
              )
            } // Add the icon here
            size="small"
            // onClick={() => <ViewDownload />}
            // onClick={() => toggleButtonCheckoutState(rowData.ID)}
            // onClick={(event) => {
            //   // (async function() {
            //   let formData = new FormData();
            //   formData.append("H_SURNAME", rowData.ID);
            //   dispatch(CheckoutActions.checkOut(formData));
            //   // });
            // }}
            onClick={async () => {
              //todo

              // alert(JSON.stringify(rowData));

              const parts = rowData.EMP.split(" : ");
              const name = parts[0];
              //const email = parts[1];

              setvisitorheader({
                ...visitorheader,
                vEmployee: name,
                vEmail: rowData.MAIL,
                vImage: rowData.IMAGE,
                vMeetdate: rowData.DATE,
                vMeettime: rowData.TIME,
                vRemark: rowData.REMARK,
                vStatus: rowData.STATUS,
                vMeetdateout: rowData.DATEOUT,
                vMeettimeout: rowData.TIMEOUT,
                vCheckindate: rowData.CHECKIN,
                vCheckoutdate: rowData.CHECKOUT,
                vCheckintime: rowData.CHECKINTIME,
                vCheckouttime: rowData.CHECKOUTTIME,
                vRoom: rowData.ROOM,
              });

              /* 
vCheckindate: rowData.STATUS,
                vCheckintime: rowData.STATUS,
                vCheckoutdate: rowData.STATUS,
                vCheckouttime: rowData.STATUS,
              */

              console.log(visitorheader.vImage);
              // let formData = new FormData();
              // formData.append("vID", rowData.ID);
              // formData.append("vStatuscheck", "CHECKOUT");
              // alert(JSON.stringify(formData));
              // await dispatch(CheckoutActions.checkOut(formData));
              await dispatch(followerActions.getFollower(rowData.ID));
              await dispatch(getimageActions.getImage(rowData.ID));

              setIDOPERATOR(rowData.ID);

              setvisitordialog({
                ...visitordialog,
                vCard: rowData.CARD,
                vRoom: rowData.ROOM,
                vEmployee: rowData.EMP,
                vEmployeedialog: rowData.EMP,
              });

              if (rowData.STATUS == "50") {
                setUpdate1(false);
              } else if (rowData.STATUS == "80") {
                setUpdate1(true);
              } else if (rowData.STATUS == "10") {
                setUpdate1(false);
              } else {
                setUpdate1(true);
              }

              // alert(rowData.STATUS);
              if (rowData.STATUS == "10") {
                setCheckin(true);
              } else {
                setCheckin(false);
              }

              setOpen(true);

              // let base64_to_imgsrc = Buffer.from(
              //   base64String,
              //   "base64"
              // ).toString();
              // setImgSrc(base64_to_imgsrc);

              if (rowData.USER == "VISITOR") {
                setUpdate1(false);
                setCheckin(false);
                setCreate(false);
              }
            }}
          >
            INFO
          </Button>
        ),
    },
    {
      title: "ID",
      field: "ID",
      headerStyle: {
        maxWidth: 100,
        whiteSpace: "nowrap",
        textAlign: "center",
        backgroundColor: "#e81e26",
      },
      cellStyle: {
        textAlign: "center",
        borderLeft: 1,
        borderRight: 1,
        borderBottom: 1,
        borderTop: 1,
        borderColor: "#E0E0E0",
        borderStyle: "solid",
        paddingLeft: "6px",
        paddingRight: "6px",
        paddingBottom: "12px",
        paddingTop: "12px",
        editable: "never",
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.ID}
        </Typography>
      ),
    },
    {
      title: "License",
      field: "LICENSE",
      headerStyle: {
        maxWidth: 100,
        whiteSpace: "nowrap",
        textAlign: "center",
        backgroundColor: "#e81e26",
      },
      cellStyle: {
        textAlign: "center",
        borderLeft: 1,
        borderRight: 1,
        borderBottom: 1,
        borderTop: 1,
        borderColor: "#E0E0E0",
        borderStyle: "solid",
        paddingLeft: "6px",
        paddingRight: "6px",
        paddingBottom: "12px",
        paddingTop: "12px",
        editable: "never",
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.LICENSE}
        </Typography>
      ),
    },
    {
      title: "NAME",
      field: "NAME",
      headerStyle: {
        maxWidth: 100,
        whiteSpace: "nowrap",
        textAlign: "center",
        backgroundColor: "#e81e26",
      },
      cellStyle: {
        textAlign: "center",
        borderLeft: 1,
        borderRight: 1,
        borderBottom: 1,
        borderTop: 1,
        borderColor: "#E0E0E0",
        borderStyle: "solid",
        paddingLeft: "6px",
        paddingRight: "6px",
        paddingBottom: "12px",
        paddingTop: "12px",
        editable: "never",
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.NAME}
        </Typography>
      ),
    },
    {
      title: "SURNAME",
      field: "SURNAME",
      headerStyle: {
        maxWidth: 100,
        whiteSpace: "nowrap",
        textAlign: "center",
        backgroundColor: "#e81e26",
      },
      cellStyle: {
        textAlign: "center",
        borderLeft: 1,
        borderRight: 1,
        borderBottom: 1,
        borderTop: 1,
        borderColor: "#E0E0E0",
        borderStyle: "solid",
        paddingLeft: "6px",
        paddingRight: "6px",
        paddingBottom: "12px",
        paddingTop: "12px",
        editable: "never",
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.SURNAME}
        </Typography>
      ),
    },
    // {
    //   title: "EMPTEST",
    //   field: "EMPTEST",
    //   editComponent: (props) => (
    //     <TextField
    //       fullWidth
    //       select
    //       multiline
    //       size="small"
    //       variant="outlined"
    //       id="vEmployee"
    //       label="ผู้ติดต่อ"
    //       SelectProps={{
    //         native: true,
    //       }}
    //       value={visitorheader.vEmployee}
    //       values={(values.vEmployee = visitorheader.vEmployee)}
    //       onChange={(event) => {
    //         let text = event.target.value;
    //         const myArray = text.split(":");

    //         setvisitorheader({
    //           ...visitorheader,
    //           vEmployee: myArray[0].trim(),
    //           vEmployee: event.target.value,
    //           vMail: myArray[1].trim(),
    //         });
    //       }}
    //     >
    //       <option />
    //       {employeeset.map((option) => (
    //         <option key={option.NAMELIST} value={option.NAMELIST}>
    //           {option.NAMELIST}
    //         </option>
    //       ))}
    //     </TextField>
    //   ),
    // },
    {
      title: "EMP",
      field: "EMP",
      headerStyle: {
        maxWidth: 100,
        whiteSpace: "nowrap",
        textAlign: "center",
        backgroundColor: "#e81e26",
      },
      cellStyle: {
        textAlign: "center",
        borderLeft: 1,
        borderRight: 1,
        borderBottom: 1,
        borderTop: 1,
        borderColor: "#E0E0E0",
        borderStyle: "solid",
        paddingLeft: "6px",
        paddingRight: "6px",
        paddingBottom: "12px",
        paddingTop: "12px",
        editable: "never",
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.EMP}
        </Typography>
      ),
    },
    {
      title: "CARD",
      field: "CARD",
      headerStyle: {
        maxWidth: 100,
        whiteSpace: "nowrap",
        textAlign: "center",
        backgroundColor: "#e81e26",
      },
      cellStyle: {
        textAlign: "center",
        borderLeft: 1,
        borderRight: 1,
        borderBottom: 1,
        borderTop: 1,
        borderColor: "#E0E0E0",
        borderStyle: "solid",
        paddingLeft: "6px",
        paddingRight: "6px",
        paddingBottom: "12px",
        paddingTop: "12px",
        editable: true,
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.CARD}
        </Typography>
      ),
    },
    {
      title: "ROOM",
      field: "ROOM",
      headerStyle: {
        maxWidth: 100,
        whiteSpace: "nowrap",
        textAlign: "center",
        backgroundColor: "#e81e26",
      },
      cellStyle: {
        textAlign: "center",
        borderLeft: 1,
        borderRight: 1,
        borderBottom: 1,
        borderTop: 1,
        borderColor: "#E0E0E0",
        borderStyle: "solid",
        paddingLeft: "6px",
        paddingRight: "6px",
        paddingBottom: "12px",
        paddingTop: "12px",
        editable: true,
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.ROOM}
        </Typography>
      ),
    },
    {
      title: "Status",
      field: "STATUS",
      headerStyle: {
        maxWidth: 100,
        whiteSpace: "nowrap",
        textAlign: "center",
        backgroundColor: "#e81e26",
      },
      cellStyle: {
        textAlign: "center",
        borderLeft: 1,
        borderRight: 1,
        borderBottom: 1,
        borderTop: 1,
        borderColor: "#E0E0E0",
        borderStyle: "solid",
        paddingLeft: "6px",
        paddingRight: "6px",
        paddingBottom: "12px",
        paddingTop: "12px",
        editable: "never",
        // color: "red",
        // backgroundColor:"blue"
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.STATUS == "99"
            ? "CANCEL"
            : item.STATUS == "10"
            ? "INITIAL"
            : item.STATUS == "15"
            ? "CHECKIN"
            : item.STATUS == "20"
            ? "Wait for Acknowlege"
            : item.STATUS == "30"
            ? "IN　PROCESS..."
            : item.STATUS == "40"
            ? "CHECKOUT"
            : item.STATUS == "80"
            ? "REJECT"
            : item.STATUS == "50"
            ? "COMPLETE"
            : item.STATUS == "30"
            ? "APPRIVE"
            : item.STATUS}
        </Typography>
      ),
    },

    {
      headerStyle: {
        maxWidth: 30,
        whiteSpace: "nowrap",
        textAlign: "center",
        backgroundColor: "#e81e26",
      },
      cellStyle: {
        textAlign: "center",
        borderLeft: 1,
        borderRight: 1,
        borderBottom: 1,
        borderTop: 1,
        borderColor: "#E0E0E0",
        borderStyle: "solid",
        paddingLeft: "6px",
        paddingRight: "6px",
        paddingBottom: "12px",
        paddingTop: "12px",
        editable: "never",
      },
      editable: false,
      render: (rowData) =>
        rowData.NAME !== "Follower" && (
          <Button
            disabled={
              rowData.USER == "ADMIN"
                ? rowData.STATUS == "15"
                  ? false
                  : rowData.STATUS == "20"
                  ? false
                  : rowData.STATUS == "80"
                  ? false
                  : true
                : true
            }
            variant="contained"
            startIcon={
              rowData.STATUS == "15" ? (
                <EmailIcon />
              ) : rowData.STATUS == "20" ? (
                <EmailIconsend style={{ color: green[500] }} />
              ) : (
                <EmailIcon />
              )
            }
            //todo
            size="small"
            // onClick={() => <ViewDownload />}
            onClick={async () =>
              //todo send email
              {
                setLoadtable(true);
                await dispatch(
                  sendmailActionspp.SendEmail(
                    // item.MHPREF,
                    // item.MARNUMBER,
                    // item.MHSTAT,
                    "EMP",
                    rowData.ID,
                    "10",
                    "Resend"
                  )
                );

                // await dispatch(operationdataActions.getOperationdata());
                await dispatch(
                  operationdataActions.getOperationfilterdata(fromdate, todate)
                );

                alert("Send email completed");
                setLoadtable(false);
                setSendemail();
              }
            }
          >
            {rowData.STATUS == "15"
              ? "SENDEMAIL"
              : rowData.STATUS == "20"
              ? "RESEND"
              : "SENDEMAIL"}
          </Button>
        ),
    },
    {
      headerStyle: {
        maxWidth: 30,
        whiteSpace: "nowrap",
        textAlign: "center",
        backgroundColor: "#e81e26",
      },
      cellStyle: {
        textAlign: "center",
        borderLeft: 1,
        borderRight: 1,
        borderBottom: 1,
        borderTop: 1,
        borderColor: "#E0E0E0",
        borderStyle: "solid",
        paddingLeft: "6px",
        paddingRight: "6px",
        paddingBottom: "12px",
        paddingTop: "12px",
        editable: "never",
      },
      render: (rowData) =>
        rowData.NAME !== "Follower" && (
          <Button
            disabled={
              rowData.USER == "ADMIN"
                ? rowData.STATUS == "30"
                  ? false
                  : true
                : true
            }
            variant="contained"
            startIcon={<CheckCircleIcon />}
            size="small"
            // onClick={() => <ViewDownload />}
            // onClick={() => toggleButtonCheckoutState(rowData.ID)}
            // onClick={(event) => {
            //   // (async function() {
            //   let formData = new FormData();
            //   formData.append("H_SURNAME", rowData.ID);
            //   dispatch(CheckoutActions.checkOut(formData));
            //   // });
            // }}
            onClick={async () => {
              // alert(rowData.ID);
              let formData = new FormData();
              formData.append("vID", rowData.ID);
              formData.append("vStatuscheck", "CHECKOUT");
              formData.append("vCheckout", visitorheader.vCheckouttime);
              formData.append("vCheckouttime", visitorheader.vCheckouttime);

              //alert(JSON.stringify(formData));
              await dispatch(CheckoutActions.checkOut(formData));
              // await dispatch(operationdataActions.getOperationdata());
              await dispatch(
                operationdataActions.getOperationfilterdata(fromdate, todate)
              );
            }}
          >
            CHECKOUT
          </Button>
        ),
    },
  ];

  // toggleButtonCheckoutState(vID);
  // {
  //   let formData = new FormData();
  //   formData.append("vID", vID);
  //   dispatch(CheckoutActions.checkOut(formData, props.history));
  // }
  //

  const style = {
    // position: "absolute",
    // top: "50%",
    // left: "50%",
    // transform: "translate(-50%, -50%)",
    // bgcolor: "background.paper",
    // border: "2px solid #000",
    // boxShadow: 24,
    // p: 4,
    // margin: 1,
  };

  const [open, setOpen] = React.useState(false);

  const handleClose = () => setOpen(false);

  const [idoperator, setIDOPERATOR] = React.useState("24000000");

  const [isadmin, setISADMIN] = React.useState("false");

  const initialvisitorheader = {
    vCono: "BangkokRanch",
    vDivi: "",
    vID: "00000000",
    vIMG: "",
    vTimein: "",
    vTimeout: "",
    vLicense: "",
    vName: "",
    vSurname: "",
    vTel: "",
    vReason: "",
    vEmployee: "",
    vCompany: "",
    vMeetdate: "-",
    vMeettime: "-",
    vEmail: "",
    vRemark: "",
    vRemark2: "-",
    vCheckindate: "",
    vCheckoutdate: "",
    vCheckintime: "",
    vCheckouttime: "",
  };

  const initialvisitordialog = {
    vEmployeedialog: "",
    vCard: "-",
    vRoom: "-",
    vEmail: "-",
  };

  const [visitordialog, setvisitordialog] = useState(initialvisitordialog);

  const [visitorheader, setvisitorheader] = useState(initialvisitorheader);
  const [selectedTeam, setSelectedTeam] = useState(null);

  const [file, setFile] = useState(null);

  const columnsFollower = [
    {
      title: "ชื่อ-สกุล",
      field: "H_SURNAME",
      headerStyle: { whiteSpace: "nowrap", textAlign: "center" },
      cellStyle: {
        textAlign: "center",
        borderLeft: 1,
        borderRight: 1,
        borderBottom: 1,
        borderTop: 1,
        borderColor: "#E0E0E0",
        borderStyle: "solid",
        paddingLeft: "6px",
        paddingRight: "6px",
        paddingBottom: "12px",
        paddingTop: "12px",
        flex: 1,
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.H_SURNAME}
        </Typography>
      ),
    },
    // {
    //   title: "Action",
    //   field: "action",
    //   headerStyle: { textAlign: "center" },
    //   cellStyle: { textAlign: "center" },
    //   render: (item) => (
    //     <IconButton
    //       onClick={() => handleDelete(item)}
    //       color="secondary"
    //       aria-label="delete"
    //     >
    //       <DeleteIcon />
    //     </IconButton>
    //   ),
    // },
  ];

  const showOperatorForm = ({
    values,
    handleChange,
    handleSubmit,
    setFieldValue,
    isSubmitting,
  }) => {
    return (
      <form onSubmit={handleSubmit}>
        <ThemeProvider theme={theme}>
          <Grid container style={{ marginBottom: 2 }} spacing={3}>
            <Grid item xs={12}>
              <div>
                <Modal open={open} onClose={handleClose}>
                  <Box
                    sx={{
                      position: "absolute",
                      top: "50%",
                      left: "50%",
                      transform: "translate(-50%, -50%)",
                      width: 600,
                      maxHeight: "80vh", // Adjust as needed
                      bgcolor: "#efe5d1",
                      boxShadow: 24,
                      p: 4,
                      overflow: "auto",
                      outline: "none",
                      color: "red",
                    }}
                  >
                    <IconButton
                      aria-label="close"
                      onClick={handleClose}
                      sx={{
                        position: "absolute",
                        right: 8,
                        top: 8,
                        color: (theme) => theme.palette.grey[500],
                      }}
                    >
                      <CloseIcon />
                    </IconButton>
                    <Typography variant="h6" component="h2" gutterBottom>
                      <Typography
                        id="modal-modal-title"
                        variant="h6"
                        component="h2"
                      >
                        ID: {idoperator}
                      </Typography>
                    </Typography>
                    <Grid container spacing={2}>
                      <Grid item xs={12}>
                        <Box
                          sx={{
                            bgcolor: "transparent",
                            color: "primary.contrastText",
                            p: 2,
                            textAlign: "center",
                          }}
                        >
                          <img
                            width={150}
                            height={150}
                            src={`data:image/png;base64,${visitorheader.vImage}`}
                            // alt=""
                            style={{
                              marginBottom: 16,
                              cursor: "pointer",
                              border: "8px solid #cdb590",
                              borderRadius: "25px",
                            }} // Add cursor pointer for better UX
                          />
                        </Box>
                      </Grid>

                      <Grid container spacing={2}>
                        {/* Check-in Section */}
                        <Grid item xs={12}>
                          <Typography variant="h6" gutterBottom>
                            CHECK-IN
                          </Typography>
                          <Grid container spacing={2}>
                            {/* Check-in Date */}
                            <Grid item xs={6}>
                              <TextField
                                fullWidth
                                disabled={!checkin}
                                type="date"
                                size="small"
                                variant="outlined"
                                id="vCheckindate"
                                label="CHECK-IN DATE (วันที่เช็คอิน)"
                                value={visitorheader.vCheckindate}
                                onChange={(event) => {
                                  setvisitorheader({
                                    ...visitorheader,
                                    vCheckindate: event.target.value,
                                  });
                                }}
                              />
                            </Grid>

                            {/* Check-in Time */}
                            <Grid item xs={6}>
                              <TextField
                                fullWidth
                                disabled={!checkin}
                                type="time"
                                size="small"
                                variant="outlined"
                                id="vCheckintime"
                                label="CHECK-IN TIME (เวลาที่เช็คอิน)"
                                value={visitorheader.vCheckintime}
                                onChange={(event) => {
                                  setvisitorheader({
                                    ...visitorheader,
                                    vCheckintime: event.target.value,
                                  });
                                }}
                              />
                            </Grid>
                          </Grid>
                        </Grid>

                        {/* Check-out Section */}
                        <Grid item xs={12} sx={{ mt: 2 }}>
                          <Typography variant="h6" gutterBottom>
                            CHECK-OUT
                          </Typography>
                          <Grid container spacing={2}>
                            {/* Check-out Date */}
                            <Grid item xs={6} spacing={1}>
                              <TextField
                                fullWidth
                                disabled={!update1}
                                type="date"
                                size="small"
                                variant="outlined"
                                id="vCheckoutdate"
                                label="CHECK-OUT DATE (วันที่เช็คเอ้าท์)"
                                value={visitorheader.vCheckoutdate}
                                onChange={(event) => {
                                  setvisitorheader({
                                    ...visitorheader,
                                    vCheckoutdate: event.target.value,
                                  });
                                }}
                              />
                            </Grid>

                            {/* Check-out Time */}
                            <Grid item xs={6} spacing={1}>
                              <TextField
                                fullWidth
                                disabled={!update1}
                                type="time"
                                size="small"
                                variant="outlined"
                                id="vCheckouttime"
                                label="CHECK-OUT TIME (เวลาที่เช็คเอ้าท์)"
                                value={visitorheader.vCheckouttime}
                                onChange={(event) => {
                                  setvisitorheader({
                                    ...visitorheader,
                                    vCheckouttime: event.target.value,
                                  });
                                }}
                              />
                            </Grid>
                          </Grid>
                        </Grid>
                      </Grid>

                      {/* <Grid item xs={12}>
                        <Box
                          sx={{
                            bgcolor: "transparent",
                            color: "primary.contrastText",
                            p: 2,
                            textAlign: "center",
                          }}
                        >
                          <TextField
                            fullWidth
                            size="small"
                            variant="outlined"
                            id="vRoom"
                            label="MEETROOM"
                            SelectProps={{
                              native: true,
                            }}
                            // helperText="Please select your order"
                            value={visitorheader.vRoom}
                            values={(values.vRoom = visitorheader.vRoom)}
                            onChange={(event) => {
                              setvisitorheader({
                                ...visitorheader,
                                vRoom: event.target.value,
                              });
                            }}
                          ></TextField>
                        </Box>
                      </Grid> */}
                      <Grid item xs={6}>
                        <Box
                          sx={{
                            bgcolor: "transparent",
                            color: "primary.contrastText",
                            p: 2,
                            textAlign: "center",
                          }}
                        >
                          <Grid spacing={2}>
                            <TextField
                              fullWidth
                              disabled={!checkin}
                              type="date"
                              size="small"
                              variant="outlined"
                              id="vMeetdate"
                              label="Meeting Date (วันนัดเข้าพบ) "
                              SelectProps={{
                                native: true,
                              }}
                              value={visitorheader.vMeetdate}
                              // values={(values.vCompany = visitorheader.vCompany)}
                              onChange={(event) => {
                                setvisitorheader({
                                  ...visitorheader,
                                  vMeetdate: event.target.value,
                                });
                              }}
                            ></TextField>
                          </Grid>{" "}
                          <br></br>
                          <Grid spacing={2}>
                            <TextField
                              fullWidth
                              disabled={!checkin}
                              type="date"
                              size="small"
                              variant="outlined"
                              id="vMeetdateout"
                              label="Meetingout Date (วันสิ้นสุดการเข้าพบ) "
                              SelectProps={{
                                native: true,
                              }}
                              value={visitorheader.vMeetdateout}
                              // values={(values.vCompany = visitorheader.vCompany)}
                              onChange={(event) => {
                                setvisitorheader({
                                  ...visitorheader,
                                  vMeetdateout: event.target.value,
                                });
                              }}
                            ></TextField>
                          </Grid>{" "}
                          <br></br>
                          <Grid spacing={2}>
                            <TextField
                              fullWidth
                              size="small"
                              variant="outlined"
                              id="vCard"
                              label="CARD"
                              SelectProps={{
                                native: true,
                              }}
                              value={visitordialog.vCard}
                              // values={(values.vCompany = visitorheader.vCompany)}
                              onChange={(event) => {
                                setvisitordialog({
                                  ...visitordialog,
                                  vCard: event.target.value,
                                });
                              }}
                            ></TextField>
                          </Grid>
                          <br></br>
                          <Grid spacing={2}>
                            <TextField
                              fullWidth
                              size="small"
                              variant="outlined"
                              id="vEmployee"
                              label="Employee (ผู้ติดต่อ)"
                              SelectProps={{
                                native: true,
                              }}
                              value={visitorheader.vEmployee}
                              // values={(values.vCompany = visitorheader.vCompany)}
                              // onChange={(event) => {
                              //   setvisitorheader({
                              //     ...visitorheader,
                              //     vCompany: event.target.value,
                              //   });
                              // }}
                            ></TextField>
                          </Grid>
                        </Box>
                      </Grid>
                      <Grid item xs={6}>
                        <Box
                          sx={{
                            bgcolor: "transparent",
                            color: "secondary.contrastText",
                            p: 2,
                            textAlign: "center",
                          }}
                        >
                          <Grid spacing={2}>
                            <TextField
                              fullWidth
                              size="small"
                              disabled={!checkin}
                              type="time"
                              variant="outlined"
                              id="vMeettime"
                              label="TIME (เวลา)"
                              SelectProps={{
                                native: true,
                              }}
                              value={visitorheader.vMeettime}
                              // values={(values.vCompany = visitorheader.vCompany)}
                              onChange={(event) => {
                                setvisitorheader({
                                  ...visitorheader,
                                  vMeettime: event.target.value,
                                });
                              }}
                            ></TextField>
                          </Grid>
                          <br></br>

                          <Grid spacing={2}>
                            <TextField
                              fullWidth
                              disabled={!checkin}
                              size="small"
                              type="time"
                              variant="outlined"
                              id="vMeettimeout"
                              label="TIMEOUT (เวลา)"
                              SelectProps={{
                                native: true,
                              }}
                              value={visitorheader.vMeettimeout}
                              // values={(values.vCompany = visitorheader.vCompany)}
                              onChange={(event) => {
                                setvisitorheader({
                                  ...visitorheader,
                                  vMeettimeout: event.target.value,
                                });
                              }}
                            ></TextField>
                          </Grid>
                          <br></br>
                          <Grid spacing={2}>
                            <TextField
                              fullWidth
                              size="small"
                              variant="outlined"
                              id="vRoom"
                              label="ROOMNo. (ห้องประชุม)"
                              SelectProps={{
                                native: true,
                              }}
                              value={visitordialog.vRoom}
                              // values={(values.vCompany = visitorheader.vCompany)}
                              onChange={(event) => {
                                setvisitordialog({
                                  ...visitordialog,
                                  vRoom: event.target.value,
                                });
                              }}
                            ></TextField>
                          </Grid>
                          <br></br>
                          <Grid spacing={2}>
                            <TextField
                              fullWidthฃ
                              disabled
                              size="small"
                              variant="outlined"
                              id="vEmail"
                              label="Tel. (เบอร์ติดต่อ)"
                              SelectProps={{
                                native: true,
                              }}
                              value={visitorheader.vEmail}
                              // values={(values.vCompany = visitorheader.vCompany)}
                              onChange={(event) => {
                                visitorheader({
                                  ...visitorheader,
                                  vEmail: event.target.value,
                                });
                              }}
                            ></TextField>
                          </Grid>
                        </Box>
                      </Grid>
                    </Grid>

                    <Grid item xs={12}>
                      <Box
                        sx={{
                          bgcolor: "transparent",
                          color: "primary.contrastText",
                          p: 2,
                          textAlign: "center",
                        }}
                      >
                        <Autocomplete
                          fullWidth
                          id="vEmployeedialog"
                          options={employeeset.map((opt) => ({
                            label: opt.NAMELIST,
                            value: opt.MAIL,
                            // value: opt.NAMELIST,
                          }))}
                          renderInput={(params) => (
                            <TextField
                              {...params}
                              label="ผู้ติดต่อ"
                              variant="outlined"
                            />
                          )}
                          // getOptionLabel={(option) => option.NAMELIST}

                          // value={selectedTeam}
                          value={visitordialog.vEmployeedialog}
                          onChange={
                            (_event, newTeam) => {
                              if (newTeam !== null) {
                                let text = newTeam.label;

                                const myArray = text.split(":");

                                setvisitordialog({
                                  ...visitordialog,
                                  // vEmployeedialog: myArray[0].trim(),
                                  vEmployeedialog: newTeam.label,

                                  vEmail: newTeam.value,
                                });
                              }
                            }
                            // setSelectedTeam(myArray[0].trim());
                          }
                        />
                      </Box>
                    </Grid>
                    <Grid item xs={12}>
                      <Box
                        sx={{
                          color: "primary.contrastText",
                          p: 2,
                          textAlign: "center",
                        }}
                      >
                        <TextField
                          required
                          fullWidth
                          multiline
                          rows={4}
                          size="small"
                          variant="outlined"
                          id="vRemark"
                          label="Remark"
                          SelectProps={{
                            native: true,
                          }}
                          // helperText="Please select your order"
                          value={visitorheader.vRemark}
                          values={(values.vReason = visitorheader.vRemark)}
                          onChange={(event) => {
                            setvisitorheader({
                              ...visitorheader,
                              vRemark: event.target.value,
                            });
                          }}
                        ></TextField>
                      </Box>
                    </Grid>

                    {/* <Grid item xs={12}>
                      <Box
                        sx={{
                          color: "primary.contrastText",
                          p: 2,
                          textAlign: "center",
                        }}
                      >
                        <TextField
                          required
                          fullWidth
                          multiline
                          rows={4}
                          size="small"
                          variant="outlined"
                          id="vRemark2"
                          label="Remark2"
                          SelectProps={{
                            native: true,
                          }}
                          // helperText="Please select your order"
                          value={visitorheader.vRemark2}
                          values={(values.vReason = visitorheader.vRemark2)}
                          onChange={(event) => {
                            setvisitorheader({
                              ...visitorheader,
                              vRemark2: event.target.value,
                            });
                          }}
                        ></TextField>
                      </Box>
                    </Grid> */}

                    <Grid item xs={12}>
                      <Box
                        sx={{
                          bgcolor: "transparent",
                          color: "primary.contrastText",
                          p: 2,
                          textAlign: "center",
                          borderRadius: "2px",
                          color: "red",
                        }}
                      >
                        <MaterialTable
                          icons={
                            {
                              // Add: () => <EditIcon style={{ color: "red" }} />,
                              // Edit: () => <EditIcon style={{ color: "orange" }} />,
                              // Delete: () => <DeleteIcon style={{ color: "red" }} />
                            }
                          }
                          // editable={{
                          //   onRowAdd: (newData) =>
                          //     new Promise((resolve, reject) => {
                          //       // alert(JSON.stringify(newData));

                          //       let formData = new FormData();
                          //       formData.append("H_SURNAME", newData.H_SURNAME);
                          //       formData.append("vID", visitorheader.vID);
                          //       resolve();
                          //       setTimeout(() => {
                          //         let NAME = newData.H_SURNAME;
                          //         let ID = visitorheader.vID;

                          //         // alert(NAME);
                          //         // alert(ID);

                          //         (async function() {
                          //           // await dispatch();
                          //           // followerActions.addFollower(formData);
                          //           // await dispatch(employeeActions.getEmployee());

                          //           await dispatch(
                          //             followerActions.addFollower(formData)
                          //           );

                          //           // alert(visitorheader.vID);
                          //           await dispatch(
                          //             followerActions.getFollower(ID)
                          //           );
                          //           alert("Add Follower Complete.");
                          //         })();
                          //         resolve();
                          //       }, 1000);
                          //     }),
                          //   onRowDelete: (oldData) =>
                          //     new Promise((resolve, reject) => {
                          //       setTimeout(() => {
                          //         let ID = oldData.vID;
                          //         let REMARK1 = oldData.REMARK1;

                          //         (async function() {
                          //           await dispatch(
                          //             followerActions.deletefollower(
                          //               ID,
                          //               REMARK1
                          //             )
                          //           );
                          //           await dispatch(
                          //             followerActions.getFollower(ID)
                          //           );
                          //         })();
                          //         resolve();
                          //       }, 1000);
                          //       resolve();
                          //     }),

                          //   onRowUpdate: (newData, oldData) =>
                          //     new Promise((resolve, reject) => {
                          //       setTimeout(() => {
                          //         let formData = new FormData();
                          //         let ID = visitorheader.vID;

                          //         formData.append("vID", ID);
                          //         formData.append(
                          //           "H_SURNAME",
                          //           newData.H_SURNAME
                          //         );
                          //         formData.append(
                          //           "oldH_SURNAME",
                          //           oldData.H_SURNAME
                          //         );

                          //         (async function() {
                          //           await dispatch(
                          //             followerActions.updateFollower(formData)
                          //           );
                          //           await dispatch(
                          //             followerActions.getFollower(ID)
                          //           );
                          //         })();
                          //         resolve();
                          //       }, 1000);
                          //     }),
                          // }}
                          id="root_pr"
                          title={`ผู้ติดตาม`}
                          columns={columnsFollower}
                          data={
                            followerReducer.result
                              ? followerReducer.result
                              : [{ H_SURNAME: "xxxx" }]
                          }
                          options={{
                            pageSize: 4,
                            paging: true,
                            headerStyle: {
                              textAlign: "center",
                              borderLeft: 1,
                              borderRight: 1,
                              borderBottom: 1,
                              borderTop: 1,
                              borderColor: "#E0E0E0",
                              borderStyle: "solid",
                              paddingLeft: "6px",
                              paddingRight: "6px",
                              paddingBottom: "12px",
                              paddingTop: "12px",
                            },
                            search: false,
                            actionsColumnIndex: -1,
                            fixedColumns: {
                              // left: 2
                            },
                          }}
                        />
                      </Box>
                    </Grid>

                    <Grid item xs={12}>
                      <Box
                        sx={{
                          bgcolor: "transparent",
                          color: "primary.contrastText",
                          p: 2,
                          textAlign: "center",
                        }}
                      >
                        <Grid container justifyContent="space-between">
                          <Grid item>
                            <Button
                              variant="contained"
                              style={{
                                backgroundColor: "#4caf50",
                              }}
                              sx={{
                                "&.Mui-disabled": {
                                  backgroundColor: "#a5d6a7", // สีเมื่อปุ่มถูกปิดการใช้งาน
                                },
                              }}
                              disabled={!update1}
                              onClick={async () => {
                                setLoadtable(true);
                                setOpen(false);

                                setTimeout(async () => {
                                  let formData = new FormData();
                                  formData.append("vCard", visitordialog.vCard);
                                  formData.append("vRoom", visitordialog.vRoom);
                                  formData.append(
                                    "vEmail",
                                    visitordialog.vEmail
                                  );

                                  // alert(visitordialog.vEmail);

                                  formData.append(
                                    "vCheckin",
                                    visitorheader.vCheckindate
                                  );
                                  formData.append(
                                    "vCheckintime",
                                    visitorheader.vCheckintime
                                  );
                                  formData.append(
                                    "vCheckout",
                                    visitorheader.vCheckoutdate
                                  );
                                  formData.append(
                                    "vCheckouttime",
                                    visitorheader.vCheckouttime
                                  );

                                  formData.append(
                                    "vStatus",
                                    visitorheader.vStatus
                                  );

                                  formData.append(
                                    "vRemark",
                                    visitorheader.vRemark
                                  );
                                  formData.append(
                                    "vEmployeedialog",
                                    visitordialog.vEmployeedialog
                                  );
                                  formData.append("vID", idoperator);

                                  await dispatch(
                                    roomcardActions.updateROOMCARD(formData)
                                  );
                                  await dispatch(
                                    roomcardActions.updateEMP(formData)
                                  );
                                  await dispatch(
                                    operationdataActions.getOperationfilterdata(
                                      fromdate,
                                      todate
                                    )
                                  );

                                  setLoadtable(false);
                                }, 1000);
                              }}
                            >
                              Update
                            </Button>
                          </Grid>

                          <Grid item>
                            <Button
                              variant="contained"
                              sx={{
                                backgroundColor: "#4caf50", // สีเมื่อปุ่มเปิดใช้งาน
                                "&.Mui-disabled": {
                                  backgroundColor: "#a5d6a7", // สีเมื่อปุ่มถูกปิดการใช้งาน
                                },
                              }}
                              style={{ backgroundColor: "#24a0ed" }}
                              disabled={!checkin}
                              onClick={async () => {
                                setLoadtable(true);
                                setOpen(false);

                                setTimeout(async () => {
                                  let formData = new FormData();
                                  formData.append("vCard", visitordialog.vCard);
                                  formData.append("vRoom", visitordialog.vRoom);

                                  formData.append(
                                    "vCheckin",
                                    visitorheader.vCheckindate
                                  );
                                  formData.append(
                                    "vCheckintime",
                                    visitorheader.vCheckintime
                                  );
                                  formData.append(
                                    "vCheckout",
                                    visitorheader.vCheckoutdate
                                  );
                                  formData.append(
                                    "vCheckouttime",
                                    visitorheader.vCheckouttime
                                  );

                                  formData.append(
                                    "vEmail",
                                    visitordialog.vEmail
                                  );

                                  formData.append(
                                    "vEmployeedialog",
                                    visitordialog.vEmployeedialog
                                  );
                                  formData.append(
                                    "vRemark",
                                    visitorheader.vRemark
                                  );
                                  formData.append("vID", idoperator);
                                  formData.append(
                                    "vStatus",
                                    visitorheader.vStatus
                                  );

                                  await dispatch(
                                    roomcardActions.updateROOMCARD(formData)
                                  );
                                  await dispatch(
                                    roomcardActions.updateEMP(formData)
                                  );
                                  await dispatch(
                                    operationdataActions.getOperationfilterdata(
                                      fromdate,
                                      todate
                                    )
                                  );
                                  await dispatch(
                                    sendmailActionspp.SendEmail(
                                      "EMP",
                                      idoperator,
                                      "10",
                                      "Resend"
                                    )
                                  );

                                  setLoadtable(false);
                                }, 1000);
                              }}
                            >
                              Check In
                            </Button>
                          </Grid>

                          <Grid item>
                            <Button
                              variant="contained"
                              sx={{
                                backgroundColor: "#24a0ed", // สีเมื่อปุ่มเปิดใช้งาน
                                "&.Mui-disabled": {
                                  backgroundColor: "#a5d6a7", // สีเมื่อปุ่มถูกปิดการใช้งาน
                                },
                              }}
                              style={{ backgroundColor: "#f0ad4e" }}
                              disabled={!update1}
                              onClick={async () => {
                                // alert(rowData.ID);
                                let formData = new FormData();
                                formData.append("vID", idoperator);
                                formData.append("vStatuscheck", "CHECKOUT");
                                formData.append(
                                  "vCheckout",
                                  visitorheader.vCheckoutdate
                                );
                                formData.append(
                                  "vCheckouttime",
                                  visitorheader.vCheckouttime
                                );

                                //alert(JSON.stringify(formData));
                                await dispatch(
                                  CheckoutActions.checkOutwithdatetime(formData)
                                );
                                // await dispatch(operationdataActions.getOperationdata());
                                await dispatch(
                                  operationdataActions.getOperationfilterdata(
                                    fromdate,
                                    todate
                                  )
                                );
                                setOpen(false);
                              }}
                            >
                              Check Out
                            </Button>
                          </Grid>

                          <Grid item>
                            <Button
                              variant="outlined"
                              style={{
                                color: "black",
                                backgroundColor: "#E57373",
                              }}
                              sx={{
                                "&.Mui-disabled": {
                                  borderColor: "#E57373", // สีขอบเมื่อปุ่มถูกปิดการใช้งาน
                                  color: "#ef9a9a", // สีข้อความเมื่อปุ่มถูกปิดการใช้งาน
                                },
                              }}
                              onClick={() => {
                                const isConfirmed = window.confirm(
                                  "Are you sure you want to cancel?"
                                );

                                if (isConfirmed) {
                                  let formData = new FormData();
                                  formData.append("vStatuscheck", "CANCEL");
                                  formData.append("vID", idoperator);
                                  formData.append("vRemark", "vRemark");
                                  formData.append("vCheckout", "vCheckout");

                                  //todo

                                  (async function() {
                                    await dispatch(
                                      CheckoutActions.checkOut(formData)
                                    );
                                    await dispatch(
                                      operationdataActions.getOperationfilterdata(
                                        fromdate,
                                        todate
                                      )
                                    );
                                    setOpen(false);
                                  })();
                                } else {
                                }
                              }}
                            >
                              CANCEL
                            </Button>
                          </Grid>
                        </Grid>
                      </Box>
                    </Grid>
                  </Box>
                </Modal>
              </div>

              <Paper className={classes.paper}>
                <Grid container spacing={1}>
                  <Grid item xs={12}>
                    <Box
                      sx={{
                        bgcolor: "transparent",
                        color: "primary.contrastText",
                        p: 2,
                        textAlign: "center",
                      }}
                    >
                      <Grid container spacing={1}>
                        <Grid item xs={4}>
                          <TextField
                            fullWidth
                            size="small"
                            type="date"
                            variant="outlined"
                            id="fromdate"
                            label="FROM DATE"
                            SelectProps={{
                              native: true,
                            }}
                            focused
                            value={fromdate}
                            onChange={(event) => {
                              setFromdate(event.target.value);
                            }}
                          />
                        </Grid>
                        <Grid item xs={4}>
                          <TextField
                            fullWidth
                            size="small"
                            type="date"
                            variant="outlined"
                            id="todate"
                            label="TO DATE"
                            SelectProps={{
                              native: true,
                            }}
                            focused
                            value={todate}
                            onChange={(event) => {
                              setTodate(event.target.value);
                            }}
                          />
                        </Grid>
                        <Grid item xs={4}>
                          <Button
                            style={{
                              backgroundColor: "#881717",
                              color: "white",
                              padding: "10px 20px",
                              border: "none",
                              borderRadius: "4px",
                              cursor: "pointer",
                              width: "80%",
                            }}
                            fullWidth
                            variant="contained"
                            startIcon={<SearchIcon />}
                            size="large"
                            onClick={async () =>
                              //todo send email
                              {
                                // alert(fromdate + " : " + todate);
                                await dispatch(
                                  operationdataActions.getOperationfilterdata(
                                    fromdate,
                                    todate
                                  )
                                );
                              }
                            }
                          >
                            SEARCH
                          </Button>
                        </Grid>
                      </Grid>
                    </Box>
                  </Grid>
                </Grid>
                <MaterialTable
                  // detailPanel={(rowData) => {
                  //   return (
                  //     <MaterialTable
                  //       // detailPanel={(rowData) => {
                  //       //   return <h1> {rowData.id} </h1>;
                  //       // }}
                  //       data={[
                  //         {
                  //           id: "00000001",
                  //           license: "PP 1124",
                  //         },
                  //       ]}
                  //       columns={[
                  //         { title: " ", field: "", editable: false },
                  //         { title: " ", field: "", editable: false },
                  //         { title: "id", field: "id", editable: false },
                  //         {
                  //           title: "license",
                  //           field: "license",
                  //           editable: false,
                  //         },
                  //       ]}
                  //       parentChildData={(row, rows) =>
                  //         rows.find((a) => a.id === row.parentId)
                  //       }
                  //       options={{
                  //         // selection: true,
                  //         showTitle: false,
                  //         search: false,

                  //         paging: false,
                  //         headerStyle: {
                  //           backgroundColor: "#FFF",
                  //           color: "#FFF",
                  //           fontSize: "17px",
                  //           textAlign: "center",
                  //           fontWeight: "bold",
                  //         },
                  //         rowStyle: (rowData) => ({
                  //           // backgroundColor: !!rowData.parentId
                  //           //   ? "#EEE"
                  //           //   : "#333",
                  //           // color: !!rowData.parentId ? "#333" : "#333",
                  //           // backgroundColor: "#FFF",
                  //         }),
                  //       }}
                  //     />
                  //   );
                  // }}
                  isLoading={loadtable}
                  localization={{
                    body: {
                      editRow: { deleteText: "ต้องการยกเลิกรายการนี้?" },
                    },
                  }}
                  editable={{
                    isEditHidden: (rowData) => rowData.NAME === "Follower",
                    isDeleteHidden: (rowData) => rowData.NAME === "Follower",
                    /*
                    onRowDelete: (oldData) =>
                      new Promise((resolve, reject) => {
                        setTimeout(() => {
                          let ID = oldData.vID;
                          let REMARK1 = oldData.REMARK1;

                          let formData = new FormData();
                          formData.append("vStatuscheck", "CANCEL");
                          formData.append("vID", oldData.ID);
                          //todo

                          (async function() {
                            await dispatch(CheckoutActions.checkOut(formData));

                            await dispatch(
                              operationdataActions.getOperationdata()
                            );
                          })();

                          resolve();
                        }, 1000);
                        resolve();
                      }),
                      */
                    // onRowUpdate: (newData, oldData) =>
                    //   new Promise((resolve, reject) => {
                    //     setTimeout(() => {
                    //       let formData = new FormData();
                    //       formData.append("vCard", newData.CARD);
                    //       formData.append("vRoom", newData.ROOM);
                    //       formData.append("vID", newData.ID);
                    //       // formData.append("oldSFDIVI", oldData.SFDIVI);

                    //       // alert(JSON.stringify(newData));

                    //       (async function() {
                    //         await dispatch(
                    //           roomcardActions.updateROOMCARD(formData)
                    //         );

                    //         await dispatch(
                    //           operationdataActions.getOperationdata()
                    //         );
                    //       })();

                    //       resolve();
                    //     }, 1000);
                    //   }),
                  }}
                  title="Visitor Operation List"
                  data={
                    operationdataReducer.result
                      ? operationdataReducer.result
                      : []
                  }
                  columns={columnsOperator}
                  options={{
                    // selection: true,
                    paging: false,
                    headerStyle: {
                      backgroundColor: "#378FC3",
                      color: "#FFF",
                      fontSize: "17px",
                      textAlign: "center",
                      fontWeight: "bold",
                    },
                    rowStyle: (rowData) => ({
                      backgroundColor: !!rowData.parentId ? "#EEE" : "#333",
                      color: !!rowData.parentId ? "#333" : "#FFF",
                      backgroundColor:
                        rowData.NAME === "Follower"
                          ? "oldlace"
                          : rowData.STATUS === "10"
                          ? "#CCCCCC" //initial
                          : rowData.STATUS === "15"
                          ? "#90caf9" // checkin
                          : rowData.STATUS === "20"
                          ? "#90caf9" // Sendmail
                          : rowData.STATUS === "30"
                          ? "#fff59d" // in process
                          : rowData.STATUS === "40"
                          ? "silver"
                          : rowData.STATUS === "50"
                          ? "#80cbc4"
                          : rowData.STATUS === "80"
                          ? "#ef9a9a"
                          : rowData.STATUS === "99"
                          ? "#ef9a9a"
                          : "#ef9a9a",

                      color: rowData.NAME === "Follower" ? "black" : "black",
                    }),
                  }}
                />
              </Paper>
            </Grid>
          </Grid>
        </ThemeProvider>
      </form>
    );
  };

  return (
    <div className={classes.root}>
      {/* Header */}
      <Formik
        initialValues={{
          // vReference: "aa",
          // vOrderno: "bb",
          // vName: "cc",
          // vLine: "dd",
          // vType: "ee",
          // vRemark1: "ff",
          // vRemark2: "gg",
          // vSubmit: "hh",
          // vStatus: "ii",
          vID: "00000000",
          vSubmit: "00000000",
          vCreate: "00000000",
          vSave: "00000000",
        }}
        onSubmit={(values, { setSubmitting }) => {
          //alert("xxxxxx");
          let formData = new FormData();

          // formData.append("vMARNumber", marhead.vMARNumber);

          // formData.append("vMARNumber", marhead.vMARNumber);
          // formData.append("vPrefix", marhead.vPrefix);
          // formData.append("vReference", values.vReference);
          // formData.append("vOrderno", values.vOrderno);
          // formData.append("vName", values.vName);
          // formData.append("vLine", values.vLine);
          // formData.append("vType", values.vType);
          // formData.append("vRemark1", values.vRemark1);
          // formData.append("vRemark2", values.vRemark2);

          formData.append("vID", values.vID);

          (async function() {
            switch (values.vSubmit) {
              case "CHECKOUT":
                alert("CHECKOUT");
                // await dispatch(swrfileActions.fetchSWRFile());
                // setswrheader(initialheader);
                // handleClear();

                break;
              case "CLEAR":
                alert("CLEAR");
                // await dispatch(swrfileActions.fetchSWRFile());

                setswrheader(initialheader);
                handleClear();

                break;
              case "SAVE":
                alert(JSON.stringify(values));

                let formData = new FormData();
                formData.append("vSwrtype", values.vSwrtype);
                formData.append("vReqdate", "20240303");
                formData.append("vFinishdate", "20240303");
                formData.append("vRemark", values.vRemark);
                formData.append("vRequester", values.vRequester);
                formData.append("vDepthead", values.vDepthead);
                formData.append("vStatus", values.vStatus);

                let orderno = await dispatch(
                  headerActions.addSWRheader(formData)
                );

                alert(orderno);
                alert("SAVE");
                //await dispatch(swrdetailActions.fetchSWRdDetail());
                await dispatch(swrfileActions.fetchSWRFile());
                await dispatch(swrIDActions.doGetID());

                break;
              case "TIMEOUT":
                alert("TIMEOUT");
                // let orderno = await dispatch(swrIDActions.doGetID1());
                // alert(JSON.stringify(orderno[0].REQDATE));
                // await setswrheader({
                //   ...swrheader,
                //   vID: orderno[0].ORDERNO,
                //   vReqdate: orderno[0].REQDATE,
                // });

                handleCreate();

                break;
              case "SENDEMAIL":
                alert("SEND");

                break;

              case "Search":
                swrheader.vID = "24000001";
                break;

              case "Checkout":
                alert("CHECKOUT");
                break;

              case "Send mail":
                alert("ff");

                break;
              // await dispatch(
              //   swrfileActions.addSWRfile(formData, props.history)
              // );
              // await dispatch(swrfileActions.fetchSWRFile());
              // break;
              default:
                await dispatch(swrfileActions.fetchSWRFile());
                break;
            }

            // await dispatch(marfileActions.fetchMARFile(marhead.vMARNumber));
            // handleDialogFileClose();
          })();
        }}
      >
        {(props) => showOperatorForm(props)}
      </Formik>
    </div>
  );
};

export default FilePage;

// function async Task<ActionResult<ApiModel>> ApiCall()
// {
//   var response = await client.GetAsync(request);
// if (response.IsSuccessStatusCode)
//     {
//         var json =  response.Content;
//         var result = JsonConvert.DeserializeObject<ApiModel>(json);
//         return  Ok(result)

//          //or you have to post API model, I can only guess

//         return Ok( new ApiModel { Content= ... I dont know what});

//     }

//    return BadRequest();
// }
