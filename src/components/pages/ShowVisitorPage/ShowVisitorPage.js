import React, { useEffect, useState } from "react";
import { Formik, Form, Field } from "formik";
import MaterialTable, { MTableToolbar } from "material-table";
import { useSelector, useDispatch } from "react-redux";
import Select, { SelectChangeEvent } from "@mui/material/Select";
import MenuItem from "@mui/material/MenuItem";
import Box from "@mui/material/Box";
import SplitPane from "react-split-pane";
import Card from "@material-ui/core/Card";
import ImageUploading from "react-images-uploading";
import Autocomplete from "@mui/material/Autocomplete";
import * as sendmailActionspp from "../../../actions/sendemailpp.action";

import * as operationdataActions from "../../../actions/operatordata.action";
import { styled } from "@mui/material/styles";

import {
  Typography,
  Grid,
  Paper,
  TextField,
  Button,
  Input,
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
import * as swrfileActions from "../../../actions/swrfile.action";

import * as ShowVisitorActions from "../../../actions/showvisitor.action";

import * as orderIDActions from "../../../actions/orderID.action";

import * as VisitorHeaderActions from "../../../actions/visitorheader.action";

import * as swrIDActions from "../../../actions/swrID.action";

import * as loginActions from "../../../actions/login.action";

import * as headerActions from "../../../actions/swrheader.action";

import * as employeeActions from "../../../actions/employee.action";

import * as followerActions from "../../../actions/follower.action";

import followerReducer from "../../../reducers/follower.reducer";

import showvisitorReducer from "../../../reducers/showvisitor.reducer";

import { size, values } from "lodash";

import * as CheckoutActions from "../../../actions/checkout.action";

// const dispatch = useDispatch();
// import { createTheme } from "@mui/material/styles";

const BackgroundPaper = styled(Paper)(({ theme }) => ({
  // backgroundImage:
  //   "url(https://plus.unsplash.com/premium_photo-1676637000058-96549206fe71?q=80&w=870&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D)", // เปลี่ยน URL เป็น URL ของรูปภาพที่คุณต้องการใช้
  backgroundSize: "cover",
  backgroundRepeat: "no-repeat",
  backgroundPosition: "center",
  // width: "100%",
  // height: "100vh", // ตั้งค่าให้เต็มหน้าจอ ถ้าต้องการ
  padding: theme.spacing(2),
  display: "flex",
  justifyContent: "center",
  alignItems: "center",
  color: "white",
  backgroundColor: "#efe5d1",
}));

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

const SRMTYPE = [
  {
    value: "add",
    label: "Add",
  },
  {
    value: "modify",
    label: "Modfy",
  },
];

const SRSTYPE = [
  {
    value: "add",
    label: "Add",
  },
];

const SWRTYPE = [
  {
    value: "srs",
    label: "SRS",
  },
  {
    value: "srm",
    label: "SRM",
  },
];

const GMTYPE = [
  {
    value: "PHAISA_KUM",
    label: "PHAISA_KUM",
  },
];

const CIOTYPE = [
  {
    value: "WUTINA_ULI",
    label: "WUTINA_ULI",
  },
];

// useEffect(() => {
//   dispatch(operationdataActions.getOperationdata());
//   // alert("xxx " + operationdataReducer.result);
// }, []);

const Overlay = styled("div")({
  position: "fixed", // เปลี่ยนเป็น fixed เพื่อครอบคลุมทั้งหน้าจอ
  top: 0,
  left: 0,
  right: 0,
  bottom: 0,
  backgroundColor: "rgba(0, 0, 0, 0)", // ตั้งค่าสีดำที่มีความโปร่งแสง 50%
  zIndex: 0,
});

const ContentWrapper = styled("div")({
  position: "relative",
  zIndex: 1, // ทำให้เนื้อหาภายในอยู่เหนือ overlay
  color: "#cdb590", // ตั้งค่าสีข้อความเป็นสีขาว
  padding: 20, // เพิ่ม padding เพื่อให้เนื้อหาไม่ติดขอบ
  display: "flex",
  flexDirection: "column",
  alignItems: "center",
  justifyContent: "center",
  minHeight: "100vh",
  boxSizing: "border-box", // ทำให้แน่ใจว่า padding ถูกนับรวมในขนาดของกล่อง
});

const useStyles = makeStyles((theme) => ({
  root: {
    width: "100%",
    marginTop: 60,
  },
  imagecontainer: {
    zIndex: 1,
    border: "5px solid black", // กรอบสีขาว
    borderRadius: "10px", // ทำให้มุมของกรอบเป็นมุมโค้งมน
  },
  textField: {
    "& .MuiInputBase-input.Mui-disabled": {
      WebkitTextFillColor: "black", // ตั้งค่าสีข้อความเป็นสีขาว
      textAlign: "center", // ทำให้ข้อความอยู่ตรงกลางในแนวนอน
      display: "flex",
      alignItems: "center",
      justifyContent: "center",
      fontSize: "1.0rem", // ขยายขนาดข้อความ
    },
    "& .MuiInputBase-input": {
      color: "black", // ตั้งค่าสีข้อความใน TextField เป็นสีขาว
      textAlign: "center",
      justifyContent: "center",
    },
    "& .MuiInputLabel-root": {
      color: "black", // ตั้งค่าสี label ของ TextField เป็นสีขาว
    },
    "& .MuiOutlinedInput-notchedOutline": {
      borderColor: "black", // ตั้งค่าสี border ของ TextField เป็นสีขาว
    },
    "& .MuiOutlinedInput-root.Mui-disabled .MuiOutlinedInput-notchedOutline": {
      borderColor: "black", // ตั้งค่าสี border เป็นสีขาว
    },
  },

  paper: {
    // padding: theme.spacing(2),
    // color: theme.palette.text.secondary,
    // color: "white",
  },
  // margin: {
  //   marginTop: "0.4rem",
  //   marginRight: "0.4rem",
  //   margin: theme.spacing(0.3),
  // },
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

  const [idoperator, setIDOPERATOR] = React.useState("24000000");
  const [cono, setCono] = React.useState("10");
  const [divi, setDivi] = React.useState("101");
  const [location, setLocation] = React.useState("11");

  const initialvisitorheader = {
    vCono: "BR",
    vID: "2400000",
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
    vMeetdate: "",
    vMeettime: "",
    vEmail: "",
    vROOMNO: "-",
    vRemark: "-",
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

  const initialStateParams = {
    id: "",
    location: "",
  };

  const [params, setParams] = useState(initialStateParams);

  //handletextfield

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

  //////////////////

  //handlebutton

  const [create, setCreate] = useState(true);
  const [clear, setClear] = useState(false);
  const [save, setSave] = useState(false);
  const [update, setUpdate] = useState(false);
  const [confirm, setConfirm] = useState(false);
  const [reject, setReject] = useState(false);
  const [editdisable, setEditDisable] = useState(false);
  const [submit, setSubmit] = useState(false);

  const [locationhead, setLOCATIONHEAD] = useState("");

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

  const [visitorheader, setvisitorheader] = useState(initialvisitorheader);

  const dispatch = useDispatch();
  const classes = useStyles();

  const employeeReducer = useSelector(({ employeeReducer }) => employeeReducer);

  const employeeset = employeeReducer.result ? employeeReducer.result : [];

  const followerReducer = useSelector(({ followerReducer }) => followerReducer);

  const showvisitorReducerset = useSelector(
    ({ showvisitorReducer }) => showvisitorReducer
  );

  useEffect(() => {
    let params = props.match.params;
    // dispatch(employeeActions.getEmployee());

    setLOCATIONHEAD(params.location);
    setIDOPERATOR(params.id);

    setCono("10");
    setDivi("101");
    setLocation(params.location);
  }, []);

  useEffect(() => {
    let params = props.match.params;
    //dispatch(employeeActions.getEmployee());
    // console.log(params.id);
    // const token = query.get("token");
    // alert(token); //123

    // setParams({ id: params.id });
    // alert(params.id);

    (async function() {
      let visitordetail = await dispatch(
        ShowVisitorActions.getshowVisitor(params.id, params.location)
      );

      // alert(visitordetail[0].IMAGE);

      setvisitorheader({
        ...visitorheader,
        vID: params.id,
        vCompany: visitordetail[0].H_COMPANYNAME,
        vEmployee: visitordetail[0].H_COMPANYNAME2,
        vLicense: visitordetail[0].H_LICENSE,
        vMeetdate: visitordetail[0].H_MEETDATE,
        vMeettime: visitordetail[0].H_MEETTIME,
        vName: visitordetail[0].H_NAME,
        vSurname: visitordetail[0].H_SURNAME,
        vReason: visitordetail[0].H_REASON,
        vTel: visitordetail[0].H_TEL,
        vImg: visitordetail[0].IMAGE,
        vROOMNO:
          visitordetail[0].H_ROOMNO == "-"
            ? "-"
            : visitordetail[0].H_ROOMNO == "undefined"
            ? "-"
            : visitordetail[0].H_ROOMNO,
        vRemark: visitordetail[0].H_REMARK1,
      });
    })();

    // let visitordetail = dispatch(ShowVisitorActions.getshowVisitor(params.id));

    // alert(visitordetail);
    // alert(showvisitorReducerset);

    // setvisitorheader({
    //   ...visitorheader,
    //   vID: params.id,
    //   // vCompany: visitordetail[0].H_COMPANYNAME,
    //   // vEmployee: visitordetail[0].H_COMPANYNAME2,
    //   // vLicense: visitordetail[0].H_LICENSE,
    //   // vMeetdate: visitordetail[0].H_MEETDATE,
    //   // vMeettime: visitordetail[0].H_MEETTIME,
    //   // vName: visitordetail[0].H_NAME,
    //   // vSurname: visitordetail[0].H_SURNAME,
    //   // vReason: visitordetail[0].H_REASON,
    //   // vTel: visitordetail[0].H_TEL,
    // });

    // (async function() {
    //   await dispatch(ShowVisitorActions.getshowVisitor(params.id));
    // })();

    // const urlSearchString = window.location.search;

    // const params = new URLSearchParams(urlSearchString);

    // alert(params.get("ID"));

    // alert(this.props.match.params.id);
  }, []);

  // useEffect(() => {
  //   dispatch(followerActions.getFollower(values.vID));
  // }, []);

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
  ];

  const showVisitorForm = ({
    values,
    handleChange,
    handleSubmit,
    setFieldValue,
    isSubmitting,
  }) => {
    return (
      <form onSubmit={handleSubmit}>
        <ThemeProvider theme={theme}>
          <Paper className={classes.paper}>
            <BackgroundPaper>
              <Overlay />
              <ContentWrapper>
                <Grid container item xs={12} spacing={1}>
                  <Grid container item xs={12} spacing={1}>
                    <Grid container item xs={12} spacing={1}>
                      <Grid container justifyContent="center" spacing={2}>
                        <Grid item xs={12}>
                          <Box
                            sx={{
                              color: "primary.contrastText",
                              p: 2,
                              textAlign: "center",
                              display: "flex",
                              flexDirection: "column",
                              alignItems: "center",
                            }}
                          >
                            <div className="imagecontainer">
                              <img
                                style={{
                                  border: "8px solid #cdb590",
                                  borderRadius: "10px",
                                }}
                                width={300}
                                height={300}
                                src={`data:image/png;base64,${visitorheader.vImg}`}
                                alt=""
                              />
                            </div>
                          </Box>
                        </Grid>
                        <Grid container item xs={12} spacing={1}>
                          <h2> Name (ชื่อ) </h2>
                          <Grid item xs={12}>
                            <TextField
                              fullWidth
                              className={classes.textField}
                              size="small"
                              variant="outlined"
                              id="vName"
                              value={visitorheader.vName}
                              disabled
                              onChange={(event) => {
                                setvisitorheader({
                                  ...visitorheader,
                                  vName: event.target.value,
                                });
                              }}
                            />
                          </Grid>
                          <Grid item xs={12}>
                            <h2>Surname (นามสกุล)</h2>
                            <TextField
                              fullWidth
                              className={classes.textField}
                              size="small"
                              variant="outlined"
                              id="vSurname"
                              value={visitorheader.vSurname}
                              disabled
                              onChange={(event) => {
                                setvisitorheader({
                                  ...visitorheader,
                                  vSurname: event.target.value,
                                });
                              }}
                            />
                          </Grid>
                        </Grid>

                        <Grid
                          container
                          item
                          xs={12}
                          spacing={1}
                          sx={{ backgroundColor: "#E0E0E0" }}
                        ></Grid>
                      </Grid>
                      <Grid container item xs={12}>
                        <h2>Meeting Room. (ระบุห้องประชุม) </h2>
                        <TextField
                          className={classes.textField}
                          fullWidth
                          size="small"
                          variant="outlined"
                          id="vROOMNO"
                          label="กรุณาระบุห้องประชุม..."
                          SelectProps={{
                            native: true,
                          }}
                          // helperText="Please select your order"
                          value={visitorheader.vROOMNO}
                          values={(values.vROOMNO = visitorheader.vROOMNO)}
                          onChange={(event) => {
                            setvisitorheader({
                              ...visitorheader,
                              vROOMNO: event.target.value,
                            });
                          }}
                        ></TextField>
                        <h2></h2>
                      </Grid>
                      <Grid container item xs={12} spacing={1}>
                        <Grid item xs={12} spacing={1}>
                          <h2>Employee (ผู้มาติดต่อ)</h2>
                        </Grid>
                        <Grid item xs={12} spacing={1}>
                          <TextField
                            className={classes.textField}
                            fullWidth
                            multiline
                            size="small"
                            variant="outlined"
                            id="vCono"
                            label="Company (บริษัท)"
                            value={visitorheader.vCono}
                            disabled
                            onChange={(event) => {
                              setvisitorheader({
                                ...visitorheader,
                                vCono: event.target.value,
                              });
                            }}
                          />
                        </Grid>
                        <Grid item xs={12} spacing={1}>
                          <TextField
                            className={classes.textField}
                            fullWidth
                            size="small"
                            variant="outlined"
                            id="vID"
                            label="ID"
                            value={visitorheader.vID}
                            disabled
                            onChange={(event) => {
                              setvisitorheader({
                                ...visitorheader,
                                vID: event.target.value,
                              });
                            }}
                          />
                        </Grid>
                      </Grid>
                      <Grid
                        container
                        item
                        xs={12}
                        spacing={1}
                        sx={{ backgroundColor: "#E0E0E0" }}
                      ></Grid>
                      <Grid item xs={12} spacing={1}>
                        <TextField
                          className={classes.textField}
                          fullWidth
                          size="small"
                          variant="outlined"
                          id="vCompany"
                          label="Company Name.(บริษัทผู้ติดต่อ)"
                          value={visitorheader.vCompany}
                          disabled
                          onChange={(event) => {
                            setvisitorheader({
                              ...visitorheader,
                              vCompany: event.target.value,
                            });
                          }}
                        />
                      </Grid>
                      <Grid item xs={12} spacing={1}>
                        <TextField
                          className={classes.textField}
                          fullWidth
                          size="small"
                          variant="outlined"
                          id="vLicense"
                          label="Vehicle licence (ทะเบียนรถ)"
                          value={visitorheader.vLicense}
                          disabled
                          onChange={(event) => {
                            setvisitorheader({
                              ...visitorheader,
                              vLicense: event.target.value,
                            });
                          }}
                        />
                      </Grid>
                      <Grid item xs={6}>
                        <TextField
                          className={classes.textField}
                          fullWidth
                          size="small"
                          type="date"
                          variant="outlined"
                          id="vMeetdate"
                          label="Meeting Date (วันนัดเข้าพบ)"
                          value={visitorheader.vMeetdate}
                          disabled
                          onChange={(event) => {
                            setvisitorheader({
                              ...visitorheader,
                              vMeetdate: event.target.value,
                            });
                          }}
                        />
                      </Grid>
                      <Grid item xs={6}>
                        <TextField
                          className={classes.textField}
                          fullWidth
                          size="small"
                          type="time"
                          variant="outlined"
                          id="vMeettime"
                          label="Meeting Time (เวลาเข้าพบ)"
                          value={visitorheader.vMeettime}
                          disabled
                          onChange={(event) => {
                            setvisitorheader({
                              ...visitorheader,
                              vMeettime: event.target.value,
                            });
                          }}
                        />
                      </Grid>

                      <Grid item xs={12} spacing={1}>
                        <TextField
                          className={classes.textField}
                          fullWidth
                          size="small"
                          variant="outlined"
                          id="vTel"
                          label="Tel. (เบอร์โทร)"
                          value={visitorheader.vTel}
                          disabled
                          onChange={(event) => {
                            setvisitorheader({
                              ...visitorheader,
                              vTel: event.target.value,
                            });
                          }}
                        />
                      </Grid>
                      <Grid item xs={12} spacing={1}>
                        <TextField
                          className={classes.textField}
                          fullWidth
                          multiline
                          rows={4}
                          size="small"
                          variant="outlined"
                          id="vReason"
                          label="Reason (เหตุผล)"
                          value={visitorheader.vReason}
                          disabled
                          onChange={(event) => {
                            setvisitorheader({
                              ...visitorheader,
                              vReason: event.target.value,
                            });
                          }}
                        />
                      </Grid>
                      <Grid item xs={12} spacing={1}>
                        <TextField
                          className={classes.textField}
                          fullWidth
                          multiline
                          rows={4}
                          size="small"
                          variant="outlined"
                          id="vRemark"
                          label="REMARK"
                          value={visitorheader.vRemark}
                          // disabled
                          onChange={(event) => {
                            setvisitorheader({
                              ...visitorheader,
                              vRemark: event.target.value,
                            });
                          }}
                        />
                      </Grid>
                    </Grid>
                    <Grid container item xs={12} spacing={1}>
                      <Grid item xs={6} spacing={3}>
                        <Button
                          fullWidth
                          size="medium"
                          type="submit"
                          id="vSubmit"
                          variant="contained"
                          style={{
                            color: "white",
                            backgroundColor: "#ccc",
                          }}
                          onClick={(event) => {
                            values.vSubmit = "REJECT";
                            values.vStatus = "01";
                          }}
                        >
                          REJECT
                        </Button>
                      </Grid>
                      <Grid item xs={6} spacing={3}>
                        <Button
                          fullWidth
                          size="medium"
                          type="submit"
                          id="vSubmit"
                          variant="contained"
                          style={{
                            color: "#fff",
                            backgroundColor: "#881717",
                          }}
                          onClick={(event) => {
                            values.vSubmit = "ACCEPT";
                            values.vStatus = "01";
                          }}
                        >
                          ACCEPT
                        </Button>
                      </Grid>
                    </Grid>
                  </Grid>
                </Grid>
                <br />
              </ContentWrapper>
            </BackgroundPaper>
          </Paper>
        </ThemeProvider>
      </form>
    );
  };

  return (
    <div className={classes.root}>
      {/* Header */}
      <Formik
        initialValues={{
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
          vStatus: "00",
          vCono: "",
          vCompany: "",
          vMeetdate: "",
          vMeettime: "",
          vMail: "",
          vROOMNO: "-",
        }}
        onSubmit={(values, { setSubmitting }) => {
          //alert("xxxxxx");
          let formData = new FormData();

          //  formData.append("vID" , visitorheader.vID);

          (async function() {
            switch (values.vSubmit) {
              case "REJECT":
                formData.append("vID", visitorheader.vID);
                formData.append("vStatuscheck", "REJECT");
                formData.append("vROOM", values.vROOMNO);
                formData.append("vLocation", locationhead);
                formData.append("vRemark", visitorheader.vRemark);

                // alert(JSON.stringify(formData));
                await dispatch(CheckoutActions.checkOut1(formData));

                console.log("SendEmailwithoutauthen");
                await dispatch(
                  sendmailActionspp.SendEmailwithoutauthen(
                    "SHOW",
                    idoperator,
                    "10",
                    "Resend",
                    cono,
                    divi,
                    location,
                    visitorheader.vMeetdate,
                    visitorheader.vMeettime,
                    visitorheader.vName,
                    visitorheader.vSurname,
                    visitorheader.vROOMNO,
                    visitorheader.vRemark
                  )
                );

                // alert(visitorheader.vMeetdate);
                // alert(visitorheader.vMeettime);
                // alert(visitorheader.vName);
                // alert(visitorheader.vSurname);
                // alert(visitorheader.vROOMNO);
                // alert(visitorheader.vRemark);

                // alert(cono);
                // alert(divi);
                // alert(location);

                // alert("REJECT Completed");
                props.history.push(
                  "/successpage/" + visitorheader.vID + "/Reject"
                );

                // await dispatch(swrfileActions.fetchSWRFile());
                //handleClear();

                break;
              case "ACCEPT":
                // await dispatch(swrfileActions.fetchSWRFile());

                formData.append("vID", visitorheader.vID);
                formData.append("vStatuscheck", "APPROVE");
                formData.append("vROOM", values.vROOMNO);
                formData.append("vLocation", locationhead);
                formData.append("vRemark", visitorheader.vRemark);
                // alert(JSON.stringify(formData));
                await dispatch(CheckoutActions.checkOut1(formData));
                alert("APPROVE Completed");
                props.history.push(
                  "/successpage/" + visitorheader.vID + "/Acception"
                );

                break;

              case "CLEAR":
                alert("CLEAR");
                // await dispatch(swrfileActions.fetchSWRFile());

                handleClear();

                break;
              case "SAVE":
                alert(JSON.stringify(values));

                //let formData = new FormData();
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

              case "SEARCH":
                alert("SEARCH");

                // let formData = new FormData();
                // formData.append("vID", values.vID);

                let ID = values.vID;

                let visitordetail = await dispatch(
                  ShowVisitorActions.getshowVisitor(ID)
                );

                // alert("details : " + visitordetail[0]);
                // alert(JSON.stringify(visitordetail[0]));

                setvisitorheader({
                  ...visitorheader,
                  vCompany: visitordetail[0].H_COMPANYNAME,
                  vEmployee: visitordetail[0].H_COMPANYNAME2,
                  vLicense: visitordetail[0].H_LICENSE,
                  vMeetdate: visitordetail[0].H_MEETDATE,
                  vMeettime: visitordetail[0].H_MEETTIME,
                  vName: visitordetail[0].H_NAME,
                  vSurname: visitordetail[0].H_SURNAME,
                  vReason: visitordetail[0].H_REASON,
                  vTel: visitordetail[0].H_TEL,
                  vROOMNO:
                    visitordetail[0].H_ROOMNO == "-"
                      ? "-"
                      : visitordetail[0].H_ROOMNO == "undefined"
                      ? "-"
                      : visitordetail[0].H_ROOMNO,

                  // vLicense:visitordetail.H_COMPANYNAME2,
                  // vLicense:visitordetail.H_COMPANYNAME2,
                  // vLicense:visitordetail.H_COMPANYNAME2,
                  // vLicense:visitordetail.H_COMPANYNAME2,
                });

                // ShowVisitorActions.showVisitor(props.history);

                // alert("cccc" + JSON.stringify(visitordetail));

                // let orderID = await dispatch(
                //   orderIDActions.addOrderID(props.history)
                // );

                // alert(orderID.ID);
                // setvisitorheader({
                //   ...visitorheader,
                //   vID: orderID.ID,
                // });

                // //alert(JSON.stringify(orderID.ID));

                // // alert(JSON.stringify(orderID[0].ID));

                // alert("ADD COMPLETE");

                // ShowVisitorActions.break;

                break;

              case "SUBMIT":
                alert("SUBMIT");
                alert(JSON.stringify(values));

                // let formData = new FormData();
                formData.append("vID", values.vID);
                formData.append("vIMG", values.vIMG);
                formData.append("vTimein", values.vTimein);
                formData.append("vTimeout", values.vTimeout);
                formData.append("vLicense", values.vLicense);
                formData.append("vName", values.vName);
                formData.append("vSurname", values.vSurname);
                formData.append("vTel", values.vTel);
                formData.append("vReason", values.vReason);
                formData.append("vEmployee", values.vEmployee);
                formData.append("vStatus", values.vStatus);
                formData.append("vCono", values.vCono);
                formData.append("vCompany", values.vCompany);
                formData.append("vMeetdate", values.vMeetdate);
                formData.append("vMeettime", values.vMeettime);
                formData.append("vEmployee", values.vEmployee);
                formData.append("vMail", values.vMail);

                await dispatch(
                  VisitorHeaderActions.addVisitorHeader(formData, props.history)
                );

                alert("Submit COMPLETE");
                break;

              // await dispatch(
              //   swrfileActions.addSWRfile(formData, props.history)
              // );
              // await dispatch(swrfileActions.fetchSWRFile());
              // break;
              default:
                break;
            }
          })();
        }}
      >
        {(props) => showVisitorForm(props)}
      </Formik>
    </div>
  );
};

export default FilePage;
