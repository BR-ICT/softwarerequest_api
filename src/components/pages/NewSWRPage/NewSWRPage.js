import React, { useEffect, useState } from "react";
import { Formik, Form, Field } from "formik";
import MaterialTable, { MTableToolbar } from "material-table";
import { useSelector, useDispatch } from "react-redux";
import Select, { SelectChangeEvent } from "@mui/material/Select";
import MenuItem from "@mui/material/MenuItem";
import Box from "@mui/material/Box";
import SplitPane from "react-split-pane";

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
import * as swrfileActions from "./../../../actions/swrfile.action";

import * as swrIDActions from "./../../../actions/swrID.action";

import * as loginActions from "./../../../actions/login.action";

import * as headerActions from "./../../../actions/swrheader.action";

import * as devActions from "./../../../actions/swrdev.action";

import * as swrfilenumberActions from "./../../../actions/swrfilenumber.action";
import { set } from "lodash";

// import { createTheme } from "@mui/material/styles";

// import swrfileReducer from "../../../reducers/swrfile.reducer";

// import swrfilenumberReducer from "../../../reducers/swrfilenumber.reducer";

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

  const [filedetail, setFileDetail] = useState(initialFileDetail);
  const [swrnumber, setswrnumber] = useState(initialswrnumber);

  const [swrheader, setswrheader] = useState(initialheader);

  const swrIDReducer = useSelector(({ swrIDReducer }) => swrIDReducer);

  const [swrsign, setswrsign] = useState(initialsign);
  const [swdetail, setswdetail] = useState(initialswdetail);

  const swrheaderrd = swrIDReducer.result ? swrIDReducer.result : [];

  const [checkType, setCheckType] = useState(true);

  const dispatch = useDispatch();
  const classes = useStyles();

  const swrfileReducer = useSelector(({ swrfileReducer }) => swrfileReducer);

  const swrfilenumberReducer = useSelector(
    ({ swrfileReducer }) => swrfileReducer
  );

  const swrheaderReducer = useSelector(
    ({ swrheaderReducer }) => swrheaderReducer
  );

  const swrdevReducer = useSelector(({ swrdevReducer }) => swrdevReducer);

  const swrheaders = swrheaderReducer.result ? swrheaderReducer.result : [];

  const swrdevs = swrdevReducer.result ? swrdevReducer.result : [];

  useEffect(() => {
    // dispatch(swrfileActions.getSWRFile());
    console.log("PPPPPP");
  }, []);

  useEffect(() => {
    dispatch(swrIDActions.doGetID());
    dispatch(headerActions.getDeptHead());
    dispatch(devActions.getDev());

    console.log("PPPPPP");
    // alert(swrIDReducer.result);
  }, []);

  useEffect(() => {
    // dispatch(swrfilenumberActions.getSWRFileNumber());
    console.log("PPPPPP");
  }, []);

  const columnsupload = [
    {
      title: "Detail",
      field: "",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.SFCONO}
        </Typography>
      ),
    },
    {
      title: "Condition/Formula",
      field: "",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.SFDIVI}
        </Typography>
      ),
    },
    {
      title: "Purpose",
      field: "",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.SFPREF}
        </Typography>
      ),
    },
  ];

  const columnsSRM = [
    {
      title: "Detail",
      field: "SLDESC3",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.SFCONO}
        </Typography>
      ),
    },
    {
      title: "Condition/Formula",
      field: "SLDESC2",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.SFDIVI}
        </Typography>
      ),
    },
    {
      title: "Purpose",
      field: "SLDESC3",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.SFPREF}
        </Typography>
      ),
    },
  ];

  const columnsFile = [
    {
      title: "Line",
      field: "SFLINE",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.SFCONO}
        </Typography>
      ),
    },
    {
      title: "Description",
      field: "SFNAME",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.SFDIVI}
        </Typography>
      ),
    },
    {
      title: "Upload",
      field: "SFREM1",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.SFPREF}
        </Typography>
      ),
    },
  ];

  const columnsSRS = [
    {
      title: "Purpose",
      field: "SLDESC1",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.SFCONO}
        </Typography>
      ),
    },
    {
      title: "Scope",
      field: "SLDESC2",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.SFDIVI}
        </Typography>
      ),
    },
    {
      title: "DescriptionS",
      field: "SLDESC3",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.SFPREF}
        </Typography>
      ),
    },
    {
      title: "Plan Development",
      field: "SLDESC4",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.SFPREF}
        </Typography>
      ),
    },
  ];

  const showFileForm = ({
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
              <Paper className={classes.paper}>
                <br></br>
                <MaterialTable
                  onRowClick={(event, data) => {
                    // alert(data.SFORNO);
                    setswrnumber({
                      ...swrnumber,
                      vOrderno: data.SFORNO,
                      vReference: data.SFPREF,
                      vName: data.SFNAME,
                      vLine: data.SFLINE,
                      vType: data.SFTYPE,
                      vRemark1: data.SFREM1,
                      vRemark2: data.SFREM2,
                    });

                    setswrheader({
                      ...swrheader,
                      vOrderno: data.SFORNO,
                    });
                  }}
                  editable={{
                    onRowDelete: (oldData) =>
                      new Promise((resolve, reject) => {
                        setTimeout(() => {
                          let SFORNO = oldData.SFORNO;

                          (async function() {
                            await dispatch(
                              swrfileActions.deleteSWRfile(SFORNO)
                            );

                            await dispatch(swrfileActions.fetchSWRFile());
                          })();

                          resolve();
                        }, 1000);
                        resolve();
                      }),

                    onRowUpdate: (newData, oldData) =>
                      new Promise((resolve, reject) => {
                        setTimeout(() => {
                          // const dataUpdate = [...data];
                          // const index = oldData.tableData.id;
                          // dataUpdate[index] = newData;
                          // setData([...dataUpdate]);

                          let formData = new FormData();
                          formData.append("oldSFORNO", oldData.SFORNO);
                          formData.append("oldSFCONO", oldData.SFCONO);
                          formData.append("oldSFDIVI", oldData.SFDIVI);

                          formData.append("newSFORNO", newData.SFORNO);
                          formData.append("newSFPREF", newData.SFPREF);
                          formData.append("newSFLINE", newData.SFLINE);
                          formData.append("newSFNAME", newData.SFNAME);
                          formData.append("newSFTYPE", newData.SFTYPE);
                          formData.append("newSFREM1", newData.SFREM1);
                          formData.append("newSFREM2", newData.SFREM2);

                          // alert(formData.get("oldSFORNO"));
                          // alert(formData.get("oldSFCONO"));
                          // alert(formData.get("oldSFDIVI"));

                          (async function() {
                            await dispatch(
                              swrfileActions.updateSWRfile(formData)
                            );

                            await dispatch(swrfileActions.fetchSWRFile());
                          })();

                          resolve();
                        }, 1000);
                      }),
                  }}
                  icons={{}}
                  id="root_pr"
                  title={`File TABLE`}
                  columns={columnsFile}
                  options={{
                    pageSize: 3,
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
                    actionsColumnIndex: -1,
                    fixedColumns: {
                      // left: 2
                    },
                  }}
                />
              </Paper>
            </Grid>
          </Grid>
        </ThemeProvider>
      </form>
    );
  };

  const showHeaderForm = ({
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
            <Grid item xs={9}>
              <Paper className={classes.paper}>
                <Grid container item xs={12} spacing={1}>
                  <Grid container item xs={12} spacing={1}>
                    <Grid container item xs={12} spacing={1}>
                      <Grid item xs={3} spacing={1}>
                        <TextField
                          select
                          fullWidth
                          // disabled={id ? editdisable : true}
                          // required
                          size="small"
                          variant="outlined"
                          id="vID"
                          label="REG ID."
                          SelectProps={{
                            native: true,
                          }}
                          // helperText="Please select your order"
                          value={swrheader.vID}
                          values={(values.vID = swrheader.vID)}
                          onChange={(event) => {
                            setswrheader({
                              ...swrheader,
                              vID: event.target.value,
                            });

                            if (event.target.value !== "") {
                              alert(event.target.value);

                              // (async function() {
                              //   await dispatch(headerActions.getLoadData());
                              // });
                              // (async function() {
                              //   let orderno = await dispatch(
                              //     swrIDActions.doGetID1()
                              //   );

                              //   alert(orderno);
                              // });
                            }
                          }}
                        >
                          <option />
                          {swrheaderrd.map((option) => (
                            <option key={option.ORDERNO} value={option.ORDERNO}>
                              {option.ORDERNO}
                            </option>
                          ))}
                        </TextField>
                      </Grid>

                      <Grid item xs={2} spacing={1}>
                        <Button
                          fullWidth
                          disabled={create ? editdisable : true}
                          type="submit"
                          id="vSubmit"
                          variant="contained"
                          color="primary"
                          onClick={(event) => {
                            values.vSubmit = "CREATE";
                            values.vStatus = "00";
                          }}
                        >
                          CREATE
                        </Button>
                      </Grid>
                      <Grid item xs={2} spacing={1}>
                        <Button
                          fullWidth
                          disabled={save ? editdisable : true}
                          type="submit"
                          id="vSubmit"
                          variant="contained"
                          color="primary"
                          onClick={(event) => {
                            values.vSubmit = "SAVE";
                            values.vStatus = "00";
                          }}
                        >
                          SAVE
                        </Button>
                      </Grid>
                      <Grid item xs={2} spacing={1}>
                        <Button
                          fullWidth
                          disabled={clear ? editdisable : true}
                          type="submit"
                          id="vClear"
                          variant="contained"
                          color="secondary"
                          onClick={(event) => {
                            values.vSubmit = "CLEAR";
                            values.vStatus = "00";
                          }}
                        >
                          CLEAR
                        </Button>
                      </Grid>
                      <Grid item xs={2} spacing={1}>
                        <Button
                          fullWidth
                          disabled={submit ? editdisable : true}
                          type="submit"
                          id="vSubmit"
                          variant="contained"
                          color="secondary"
                          onClick={(event) => {
                            values.vSubmit = "Submit";
                            values.vStatus = "00";
                          }}
                        >
                          SUBMIT
                        </Button>
                      </Grid>
                    </Grid>
                    <Grid item xs={2} spacing={1}>
                      <Button
                        fullWidth
                        type="submit"
                        id="vSubmit"
                        variant="contained"
                        color="secondary"
                        onClick={(event) => {
                          values.vSubmit = "Search";
                          values.vStatus = "00";
                        }}
                      >
                        SEARCH
                      </Button>
                    </Grid>
                    <Grid container item xs={12} spacing={1}>
                      <Grid item xs={3} spacing={1}>
                        <TextField
                          select
                          fullWidth
                          // required
                          size="small"
                          disabled={type ? editdisable : true}
                          variant="outlined"
                          id="vSwrtype"
                          label="Type"
                          SelectProps={{
                            native: true,
                          }}
                          // helperText="Please select your order"
                          value={swrheader.vSwrtype}
                          values={(values.vSwrtype = swrheader.vSwrtype)}
                          onChange={(event) => {
                            // todo\

                            if (event.target.value === "srs") {
                              setCheckType(true);
                            } else {
                              setCheckType(false);
                            }

                            setswrheader({
                              ...swrheader,
                              vSwrtype: event.target.value,
                            });
                          }}
                        >
                          {SWRTYPE.map((option) => (
                            <option key={option.value} value={option.value}>
                              {option.label}
                            </option>
                          ))}
                        </TextField>
                      </Grid>

                      <Grid item xs={6} spacing={1}>
                        <TextField
                          fullWidth
                          // required
                          disabled={swrname ? editdisable : true}
                          size="small"
                          variant="outlined"
                          id="vSwrname"
                          label="Software Name"
                          SelectProps={{
                            native: true,
                          }}
                          // helperText="Please select your order"
                          value={swrheader.vSwrname}
                          values={(values.vSwrname = swrheader.vSwrname)}
                          onChange={(event) => {
                            setswrheader({
                              ...swrheader,
                              vSwrname: event.target.value,
                            });
                          }}
                        ></TextField>
                      </Grid>
                      <Grid item xs={3} spacing={1}>
                        <TextField
                          fullWidth
                          size="small"
                          disabled={version ? editdisable : true}
                          variant="outlined"
                          id="vVersion"
                          label="Current Ver."
                          SelectProps={{
                            native: true,
                          }}
                          // helperText="Please select your order"
                          value={swrheader.vVersion}
                          values={(values.vVersion = swrheader.vVersion)}
                          onChange={(event) => {
                            setswrheader({
                              ...swrheader,
                              vVersion: event.target.value,
                            });
                          }}
                        ></TextField>
                      </Grid>
                    </Grid>

                    <Grid container item xs={12} spacing={1}>
                      <Grid item xs={3} spacing={1}>
                        <TextField
                          // disabled={editdisable}
                          fullWidth
                          // required
                          disabled={reqdate ? editdisable : true}
                          type="date"
                          size="small"
                          id="vReqdate"
                          label="Request Date"
                          variant="outlined"
                          InputLabelProps={{ shrink: true, required: true }}
                          value={swrheader.vReqdate}
                          values={(values.vReqdate = swrheader.vReqdate)}
                          onChange={(event) => {
                            setswrheader({
                              ...swrheader,
                              vReqdate: event.target.value,
                            });
                          }}
                        />
                      </Grid>
                      <Grid item xs={3} spacing={1}>
                        <TextField
                          // disabled={editdisable}
                          fullWidth
                          // required
                          disabled={findate ? editdisable : true}
                          type="date"
                          size="small"
                          id="vFinishdate"
                          label="Finish Date"
                          variant="outlined"
                          InputLabelProps={{ shrink: true, required: true }}
                          value={swrheader.vFinishdate}
                          values={(values.vFinishdate = swrheader.vFinishdate)}
                          onChange={(event) => {
                            setswrheader({
                              ...swrheader,
                              vFinishdate: event.target.value,
                            });
                          }}
                        />
                      </Grid>
                      <Grid item xs={6} spacing={1}>
                        <TextField
                          fullWidth
                          disabled={remark ? editdisable : true}
                          width="20%"
                          size="small"
                          variant="outlined"
                          id="vRemark"
                          label="Remark"
                          value={swrheader.vRemark}
                          values={(values.vRemark = swrheader.vRemark)}
                          onChange={(event) => {
                            setswrheader({
                              ...swrheader,
                              vRemark: event.target.value,
                            });
                          }}
                        ></TextField>
                      </Grid>
                      <Grid item xs={2} spacing={1}>
                        <TextField
                          // required
                          disabled
                          fullWidth
                          width="20%"
                          size="small"
                          variant="outlined"
                          id="vRequester"
                          label="Requester."
                          value={swrheader.vRequester}
                          values={(values.vRequester = swrheader.vRequester)}
                          onChange={(event) => {
                            setswrheader({
                              ...swrheader,
                              vRequester: event.target.value,
                            });
                          }}
                        ></TextField>
                      </Grid>
                      <Grid item xs={2} spacing={1}>
                        <TextField
                          fullWidth
                          select
                          // required
                          disabled={depthead ? editdisable : true}
                          width="20%"
                          size="small"
                          variant="outlined"
                          id="vDepthead"
                          label="Dept Head."
                          SelectProps={{
                            native: true,
                          }}
                          value={swrheader.vDepthead}
                          values={(values.vDepthead = swrheader.vDepthead)}
                          onChange={(event) => {
                            setswrheader({
                              ...swrheader,
                              vDepthead: event.target.value,
                            });
                          }}
                        >
                          <option />
                          {swrheaders.map((option) => (
                            <option
                              key={option.US_LOGIN}
                              value={option.US_LOGIN}
                            >
                              {option.US_LOGIN}
                            </option>
                          ))}
                        </TextField>
                      </Grid>
                      <Grid item xs={2} spacing={1}>
                        <TextField
                          select
                          // required
                          disabled={developer ? editdisable : true}
                          fullWidth
                          width="20%"
                          size="small"
                          variant="outlined"
                          id="vDev"
                          label="Developer."
                          SelectProps={{
                            native: true,
                          }}
                          value={swrheader.vDev}
                          values={(values.vDev = swrheader.vDev)}
                          onChange={(event) => {
                            setswrheader({
                              ...swrheader,
                              vDev: event.target.value,
                            });
                          }}
                        >
                          <option />
                          {swrdevs.map((option) => (
                            <option key={option.US_DEV} value={option.US_DEV}>
                              {option.US_DEV}
                            </option>
                          ))}
                        </TextField>
                      </Grid>
                      <Grid item xs={2} spacing={1}>
                        <TextField
                          // required
                          select
                          fullWidth
                          disabled={appdevmanager ? editdisable : true}
                          width="20%"
                          size="small"
                          variant="outlined"
                          id="vAppdevmanager"
                          label="App&Dev Mng."
                          SelectProps={{
                            native: true,
                          }}
                          value={swrheader.vAppdevmanager}
                          values={
                            (values.vAppdevmanager = swrheader.vAppdevmanager)
                          }
                          onChange={(event) => {
                            setswrheader({
                              ...swrheader,
                              vAppdevmanager: event.target.value,
                            });
                          }}
                        >
                          <option />
                          {swrdevs.map((option) => (
                            <option key={option.US_DEV} value={option.US_DEV}>
                              {option.US_DEV}
                            </option>
                          ))}
                        </TextField>
                      </Grid>
                      <Grid item xs={2} spacing={1}>
                        <TextField
                          // required
                          select
                          fullWidth
                          width="20%"
                          disabled={gm ? editdisable : true}
                          size="small"
                          variant="outlined"
                          id="vGM"
                          label="Asst.GM."
                          SelectProps={{
                            native: true,
                          }}
                          value={swrheader.vGM}
                          values={(values.vGM = swrheader.vGM)}
                          onChange={(event) => {
                            setswrheader({
                              ...swrheader,
                              vGM: event.target.value,
                            });
                          }}
                        >
                          {GMTYPE.map((option) => (
                            <option key={option.value} value={option.value}>
                              {option.label}
                            </option>
                          ))}
                        </TextField>
                      </Grid>
                      <Grid item xs={2} spacing={1}>
                        <TextField
                          // required
                          select
                          fullWidth
                          width="20%"
                          size="small"
                          disabled={cio ? editdisable : true}
                          variant="outlined"
                          id="vCIO"
                          label="CIO"
                          SelectProps={{
                            native: true,
                          }}
                          value={swrheader.vCIO}
                          values={(values.vCIO = swrheader.vCIO)}
                          onChange={(event) => {
                            setswrheader({
                              ...swrheader,
                              vCIO: event.target.value,
                            });
                          }}
                        >
                          {CIOTYPE.map((option) => (
                            <option key={option.value} value={option.value}>
                              {option.label}
                            </option>
                          ))}
                        </TextField>
                      </Grid>
                    </Grid>
                  </Grid>
                </Grid>
                <br />
              </Paper>
            </Grid>
            <Grid container item xs={3}>
              <Paper className={classes.paper}>
                {/* <Grid item xs={3} spacing={1} backgroundColor="#E0E0E0"> */}
                <Box
                  component="img"
                  sx={{
                    height: 233,
                    width: 350,
                    maxHeight: { xs: 233, md: 167 },
                    maxWidth: { xs: 350, md: 250 },
                  }}
                  alt="The house from the offer."
                  //src="https://images.unsplash.com/photo-1512917774080-9991f1c4c750?auto=format&w=350&dpr=2"
                  // src={"data:image/png;base64," + photoBlob}
                />
                {/* <h2>&nbsp; Request Date :　</h2>
                      <h2>&nbsp; {values.vOrderno}</h2> */}
                {/* </Grid> */}
              </Paper>
            </Grid>
          </Grid>
        </ThemeProvider>
      </form>
    );
  };

  const showDetailSRS = ({
    values,
    handleChange,
    handleSubmit,
    setFieldValue,
    isSubmitting,
  }) => {
    return (
      <form onSubmit={handleSubmit}>
        <Grid container style={{ marginBottom: 2 }} spacing={3}>
          <Grid item xs={12}>
            <Paper className={classes.paper}>
              <Grid container item xs={12} spacing={1}>
                <Grid item xs={3} spacing={1}>
                  <TextField
                    select
                    fullWidth
                    size="small"
                    variant="outlined"
                    id="vDetailtype"
                    label="SRS TYPE"
                    SelectProps={{
                      native: true,
                    }}
                    // helperText="Please select your order"
                    value={swdetail.vDetailtype}
                    values={(values.vDetailtype = swdetail.vDetailtype)}
                    onChange={(event) => {
                      // todo\

                      setswdetail({
                        ...swdetail,
                        vDetailtype: event.target.value,
                      });
                    }}
                  >
                    {SRSTYPE.map((option) => (
                      <option key={option.value} value={option.value}>
                        {option.label}
                      </option>
                    ))}
                  </TextField>
                </Grid>

                <Grid item xs={12} spacing={1}>
                  <MaterialTable
                    onRowClick={(event, data) => {
                      // alert(data.SFORNO);
                      setswrnumber({
                        ...swrnumber,
                        vOrderno: data.SFORNO,
                        vReference: data.SFPREF,
                        vName: data.SFNAME,
                        vLine: data.SFLINE,
                        vType: data.SFTYPE,
                        vRemark1: data.SFREM1,
                        vRemark2: data.SFREM2,
                      });

                      setswrheader({
                        ...swrheader,
                        vOrderno: data.SFORNO,
                      });
                    }}
                    editable={{
                      onRowDelete: (oldData) =>
                        new Promise((resolve, reject) => {
                          setTimeout(() => {
                            let SFORNO = oldData.SFORNO;

                            (async function() {
                              await dispatch(
                                swrfileActions.deleteSWRfile(SFORNO)
                              );

                              await dispatch(swrfileActions.fetchSWRFile());
                            })();

                            resolve();
                          }, 1000);
                          resolve();
                        }),

                      onRowUpdate: (newData, oldData) =>
                        new Promise((resolve, reject) => {
                          setTimeout(() => {
                            // const dataUpdate = [...data];
                            // const index = oldData.tableData.id;
                            // dataUpdate[index] = newData;
                            // setData([...dataUpdate]);

                            let formData = new FormData();
                            formData.append("oldSFORNO", oldData.SFORNO);
                            formData.append("oldSFCONO", oldData.SFCONO);
                            formData.append("oldSFDIVI", oldData.SFDIVI);

                            formData.append("newSFORNO", newData.SFORNO);
                            formData.append("newSFPREF", newData.SFPREF);
                            formData.append("newSFLINE", newData.SFLINE);
                            formData.append("newSFNAME", newData.SFNAME);
                            formData.append("newSFTYPE", newData.SFTYPE);
                            formData.append("newSFREM1", newData.SFREM1);
                            formData.append("newSFREM2", newData.SFREM2);

                            // alert(formData.get("oldSFORNO"));
                            // alert(formData.get("oldSFCONO"));
                            // alert(formData.get("oldSFDIVI"));

                            (async function() {
                              await dispatch(
                                swrfileActions.updateSWRfile(formData)
                              );

                              await dispatch(swrfileActions.fetchSWRFile());
                            })();

                            resolve();
                          }, 1000);
                        }),
                    }}
                    icons={{}}
                    id="root_pr"
                    title={`SRS TABLE`}
                    columns={columnsSRS}
                    data={
                      swrfileReducer.result
                        ? swrfileReducer.result
                        : [{ SFCONO: "xxx" }]

                      // swrfilenumberReducer.result
                      //   ? swrfilenumberReducer.result
                      //   : [{ SFCONO: "xxx" }]
                    }
                    options={{
                      pageSize: 8,
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
                      actionsColumnIndex: -1,
                      fixedColumns: {
                        // left: 2
                      },
                    }}
                  />
                </Grid>
              </Grid>
            </Paper>
          </Grid>
        </Grid>
      </form>
    );
  };

  const showDetailSRM = ({
    values,
    handleChange,
    handleSubmit,
    setFieldValue,
    isSubmitting,
  }) => {
    return (
      <form onSubmit={handleSubmit}>
        <Grid container style={{ marginBottom: 2 }} spacing={3}>
          <Grid item xs={12}>
            <Paper className={classes.paper}>
              <Grid container item xs={12} spacing={1}>
                <Grid item xs={3} spacing={1}>
                  <TextField
                    select
                    fullWidth
                    size="small"
                    variant="outlined"
                    id="vType"
                    label="SRM TYPE"
                    SelectProps={{
                      native: true,
                    }}
                    // helperText="Please select your order"
                    value={swdetail.vDetailtype}
                    values={(values.vDetailtype = swdetail.vDetailtype)}
                    onChange={(event) => {
                      // todo\

                      setswdetail({
                        ...swdetail,
                        vDetailtype: event.target.value,
                      });
                    }}
                  >
                    {SRMTYPE.map((option) => (
                      <option key={option.value} value={option.value}>
                        {option.label}
                      </option>
                    ))}
                  </TextField>
                </Grid>
                <Grid item xs={12} spacing={1}>
                  <MaterialTable
                    onRowClick={(event, data) => {
                      // alert(data.SFORNO);
                      setswrnumber({
                        ...swrnumber,
                        vOrderno: data.SFORNO,
                        vReference: data.SFPREF,
                        vName: data.SFNAME,
                        vLine: data.SFLINE,
                        vType: data.SFTYPE,
                        vRemark1: data.SFREM1,
                        vRemark2: data.SFREM2,
                      });

                      setswrheader({
                        ...swrheader,
                        vOrderno: data.SFORNO,
                      });
                    }}
                    editable={{
                      onRowDelete: (oldData) =>
                        new Promise((resolve, reject) => {
                          setTimeout(() => {
                            let SFORNO = oldData.SFORNO;

                            (async function() {
                              await dispatch(
                                swrfileActions.deleteSWRfile(SFORNO)
                              );

                              await dispatch(swrfileActions.fetchSWRFile());
                            })();

                            resolve();
                          }, 1000);
                          resolve();
                        }),

                      onRowUpdate: (newData, oldData) =>
                        new Promise((resolve, reject) => {
                          setTimeout(() => {
                            // const dataUpdate = [...data];
                            // const index = oldData.tableData.id;
                            // dataUpdate[index] = newData;
                            // setData([...dataUpdate]);

                            let formData = new FormData();
                            formData.append("oldSFORNO", oldData.SFORNO);
                            formData.append("oldSFCONO", oldData.SFCONO);
                            formData.append("oldSFDIVI", oldData.SFDIVI);

                            formData.append("newSFORNO", newData.SFORNO);
                            formData.append("newSFPREF", newData.SFPREF);
                            formData.append("newSFLINE", newData.SFLINE);
                            formData.append("newSFNAME", newData.SFNAME);
                            formData.append("newSFTYPE", newData.SFTYPE);
                            formData.append("newSFREM1", newData.SFREM1);
                            formData.append("newSFREM2", newData.SFREM2);

                            // alert(formData.get("oldSFORNO"));
                            // alert(formData.get("oldSFCONO"));
                            // alert(formData.get("oldSFDIVI"));

                            (async function() {
                              await dispatch(
                                swrfileActions.updateSWRfile(formData)
                              );

                              await dispatch(swrfileActions.fetchSWRFile());
                            })();

                            resolve();
                          }, 1000);
                        }),
                    }}
                    icons={{}}
                    id="root_pr"
                    title={`SRM TABLE`}
                    columns={columnsSRM}
                    data={
                      swrfileReducer.result
                        ? swrfileReducer.result
                        : [{ SFCONO: "xxx" }]

                      // swrfilenumberReducer.result
                      //   ? swrfilenumberReducer.result
                      //   : [{ SFCONO: "xxx" }]
                    }
                    options={{
                      pageSize: 8,
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
                      actionsColumnIndex: -1,
                      fixedColumns: {
                        // left: 2
                      },
                    }}
                  />
                </Grid>
              </Grid>
            </Paper>
          </Grid>
        </Grid>
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
              case "CREATE":
                // alert("CREATE");
                // let orderno = await dispatch(swrIDActions.doGetID1());
                // alert(JSON.stringify(orderno[0].REQDATE));
                // await setswrheader({
                //   ...swrheader,
                //   vID: orderno[0].ORDERNO,
                //   vReqdate: orderno[0].REQDATE,
                // });

                handleCreate();

                break;
              case "CREATE":
                alert("SUBMIT");

                break;

              case "Search":
                swrheader.vID = "24000001";
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
        {(props) => showHeaderForm(props)}
      </Formik>

      {/* Detail */}

      <Formik
        initialValues={{
          vReference: "aa",
          vOrderno: "bb",
          vName: "cc",
          vLine: "dd",
          vType: "ee",
          vRemark1: "ff",
          vRemark2: "gg",
        }}
        onSubmit={(values, { setSubmitting }) => {
          alert(JSON.stringify(values));
          let formData = new FormData();
          // formData.append("vMARNumber", marhead.vMARNumber);
          // formData.append("vPrefix", marhead.vPrefix);
          formData.append("vReference", values.vReference);
          formData.append("vOrderno", values.vOrderno);
          formData.append("vName", values.vName);
          formData.append("vLine", values.vLine);
          formData.append("vType", values.vType);
          formData.append("vRemark1", values.vRemark1);
          formData.append("vRemark2", values.vRemark2);

          (async function() {
            await dispatch(swrfileActions.addSWRfile(formData, props.history));
            await dispatch(swrfileActions.fetchSWRFile());

            // await dispatch(marfileActions.fetchMARFile(marhead.vMARNumber));
            // handleDialogFileClose();
          })();
        }}
      >
        {(props) => (checkType ? showDetailSRS(props) : showDetailSRM(props))}
      </Formik>

      {/* File */}
      <Formik
        initialValues={{
          vReference: "aa",
          vOrderno: "bb",
          vName: "cc",
          vLine: "dd",
          vType: "ee",
          vRemark1: "ff",
          vRemark2: "gg",
        }}
        onSubmit={(values, { setSubmitting }) => {
          alert(JSON.stringify(values));
          let formData = new FormData();
          // formData.append("vMARNumber", marhead.vMARNumber);
          // formData.append("vPrefix", marhead.vPrefix);
          formData.append("vReference", values.vReference);
          formData.append("vOrderno", values.vOrderno);
          formData.append("vName", values.vName);
          formData.append("vLine", values.vLine);
          formData.append("vType", values.vType);
          formData.append("vRemark1", values.vRemark1);
          formData.append("vRemark2", values.vRemark2);

          (async function() {
            await dispatch(swrfileActions.addSWRfile(formData, props.history));
            await dispatch(swrfileActions.fetchSWRFile());

            // await dispatch(marfileActions.fetchMARFile(marhead.vMARNumber));
            // handleDialogFileClose();
          })();
        }}
      >
        {(props) => showFileForm(props)}
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
