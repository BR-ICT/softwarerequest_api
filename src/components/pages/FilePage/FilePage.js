import React, { useEffect, useState } from "react";
import { Formik, Form, Field } from "formik";
import MaterialTable, { MTableToolbar } from "material-table";
import { useSelector, useDispatch } from "react-redux";
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

import * as swrfilenumberActions from "./../../../actions/swrfilenumber.action";

// import swrfileReducer from "../../../reducers/swrfile.reducer";

// import swrfilenumberReducer from "../../../reducers/swrfilenumber.reducer";

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
    vOrderno: "bb",
  };

  const [filedetail, setFileDetail] = useState(initialFileDetail);
  const [swrnumber, setswrnumber] = useState(initialswrnumber);
  const [swrheader, setswrheader] = useState(initialheader);

  const dispatch = useDispatch();
  const classes = useStyles();

  const swrfileReducer = useSelector(({ swrfileReducer }) => swrfileReducer);

  const swrfilenumberReducer = useSelector(
    ({ swrfileReducer }) => swrfileReducer
  );

  const swrnumbers = swrfilenumberReducer.result
    ? swrfilenumberReducer.result
    : [];

  useEffect(() => {
    dispatch(swrfileActions.getSWRFile());
    console.log("PPPPPP");
  }, []);

  useEffect(() => {
    dispatch(swrfilenumberActions.getSWRFileNumber());
    console.log("PPPPPP");
  }, []);

  const columns = [
    {
      title: "SFCONO",
      field: "SFCONO",
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
      title: "SFDIVI",
      field: "SFDIVI",
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
      title: "SFPREF",
      field: "SFPREF",
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
      title: "SFORNO",
      field: "SFORNO",
      editable: "false",
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
          {item.SFORNO}
        </Typography>
      ),
    },
    {
      title: "SFNAME",
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
          {item.SFNAME}
        </Typography>
      ),
    },
    {
      title: "SFTYPE",
      field: "SFTYPE",
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
          {item.SFTYPE}
        </Typography>
      ),
    },
    {
      title: "SFREM1",
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
          {item.SFREM1}
        </Typography>
      ),
    },
    {
      title: "SFREM2",
      field: "SFREM2",
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
          {item.SFREM2}
        </Typography>
      ),
    },
    {
      title: "SFENDA",
      field: "SFENDA",
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
          {item.SFENDA}
        </Typography>
      ),
    },
    {
      title: "SFENTI",
      field: "SFENTI",
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
          {item.SFENTI}
        </Typography>
      ),
    },
    {
      title: "SFENUS",
      field: "SFENUS",
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
          {item.SFENUS}
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
        <Grid container style={{ marginBottom: 2 }} spacing={3}>
          <Grid item xs={12}>
            <Paper className={classes.paper}>
              {/* <Grid container item xs={12} spacing={1}>
                <Grid container item xs={12} spacing={1}>
                  <Grid item xs={1} spacing={1}>
                    <TextField
                      fullWidth
                      width="20%"
                      size="small"
                      variant="outlined"
                      id="vReference"
                      label="Reference"
                      value={filedetail.vReference}
                      values={(values.vReference = filedetail.vReference)}
                      onChange={(event) => {
                        setFileDetail({
                          ...filedetail,
                          vReference: event.target.value,
                        });
                      }}
                    ></TextField>
                  </Grid>

                  <Grid item xs={1} spacing={1}>
                    <TextField
                      fullWidth
                      width="20%"
                      size="small"
                      variant="outlined"
                      id="vOrderno"
                      label="Order No."
                      value={filedetail.vOrderno}
                      values={(values.vOrderno = filedetail.vOrderno)}
                      onChange={(event) => {
                        setFileDetail({
                          ...filedetail,
                          vOrderno: event.target.value,
                        });
                      }}
                    ></TextField>
                  </Grid>

                  <Grid item xs={1} spacing={1}>
                    <TextField
                      fullWidth
                      width="20%"
                      size="small"
                      variant="outlined"
                      id="vName"
                      label="NAME"
                      value={filedetail.vName}
                      values={(values.vName = filedetail.vName)}
                      onChange={(event) => {
                        setFileDetail({
                          ...filedetail,
                          vName: event.target.value,
                        });
                      }}
                    ></TextField>
                  </Grid>
                  <Grid item xs={1} spacing={1}>
                    <TextField
                      fullWidth
                      width="20%"
                      size="small"
                      variant="outlined"
                      id="vLine"
                      label="LINE"
                      value={filedetail.vLine}
                      values={(values.vLine = filedetail.vLine)}
                      onChange={(event) => {
                        setFileDetail({
                          ...filedetail,
                          vLine: event.target.value,
                        });
                      }}
                    ></TextField>
                  </Grid>
                  <Grid item xs={1} spacing={1}>
                    <TextField
                      fullWidth
                      width="20%"
                      size="small"
                      variant="outlined"
                      id="vType"
                      label="TYPE"
                      value={filedetail.vType}
                      values={(values.vType = filedetail.vType)}
                      onChange={(event) => {
                        setFileDetail({
                          ...filedetail,
                          vType: event.target.value,
                        });
                      }}
                    ></TextField>
                  </Grid>
                  <Grid item xs={1} spacing={1}>
                    <TextField
                      fullWidth
                      width="20%"
                      size="small"
                      variant="outlined"
                      id="vRemark1"
                      label="REMARK 1"
                      value={filedetail.vRemark1}
                      values={(values.vRemark1 = filedetail.vRemark1)}
                      onChange={(event) => {
                        setFileDetail({
                          ...filedetail,
                          vRemark1: event.target.value,
                        });
                      }}
                    ></TextField>
                  </Grid>
                  <Grid item xs={1} spacing={1}>
                    <TextField
                      required
                      fullWidth
                      width="20%"
                      size="small"
                      variant="outlined"
                      id="vRemark2"
                      label="REMARK 2"
                      value={filedetail.vRemark2}
                      values={(values.vRemark2 = filedetail.vRemark2)}
                      onChange={(event) => {
                        setFileDetail({
                          ...filedetail,
                          vRemark2: event.target.value,
                        });
                      }}
                    ></TextField>
                  </Grid>
                  <Grid item XS={1} md={1}>
                    <Button
                      fullWidth
                      type="submit"
                      id="vSubmit"
                      variant="contained"
                      color="primary"
                    >
                      Submit
                    </Button>
                  </Grid>
                </Grid>
              </Grid> */}
              <br />

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
                          await dispatch(swrfileActions.deleteSWRfile(SFORNO));

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
                title={`SWR TABLE`}
                columns={columns}
                data={
                  swrfileReducer.result
                    ? swrfileReducer.result
                    : [{ SFCONO: "xxx" }]

                  // swrfilenumberReducer.result
                  //   ? swrfilenumberReducer.result
                  //   : [{ SFCONO: "xxx" }]
                }
                options={{
                  pageSize: 10,
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
        <Grid container style={{ marginBottom: 2 }} spacing={3}>
          <Grid item xs={12}>
            <Paper className={classes.paper}>
              <Grid container item xs={12} spacing={1}>
                <Grid container item xs={12} spacing={1}>
                  <Grid container item xs={12} spacing={1}>
                    <Grid container item xs={6} spacing={1}>
                      <TextField
                        fullWidth
                        width="20%"
                        size="small"
                        variant="outlined"
                        id="vSwrname"
                        label="Software Name"
                        SelectProps={{
                          native: true,
                        }}
                        helperText="Please type software name"
                        value={swrheader.vOrderno}
                        values={(values.vOrderno = swrnumber.vOrderno)}
                        onChange={(event) => {
                          // todo

                          setswrheader({
                            ...swrheader,
                            vOrderno: event.target.value,
                          });
                        }}
                      >
                        <option />
                      </TextField>
                    </Grid>
                    <Grid container item xs={6} spacing={1}>
                      <h2>&nbsp; Current Version :　</h2>
                      <h2>&nbsp; {values.vOrderno}</h2>
                    </Grid>
                  </Grid>
                  <Grid container item xs={12} spacing={1}>
                    <Grid item xs={6} spacing={1}>
                      <TextField
                        fullWidth
                        select
                        width="20%"
                        size="small"
                        variant="outlined"
                        id="vOrderno"
                        label="Order No."
                        SelectProps={{
                          native: true,
                        }}
                        helperText="Please select your order"
                        value={swrheader.vOrderno}
                        values={(values.vOrderno = swrnumber.vOrderno)}
                        onChange={(event) => {
                          // todo

                          setswrheader({
                            ...swrheader,
                            vOrderno: event.target.value,
                          });
                        }}
                      >
                        <option />
                        {swrnumbers.map((option) => (
                          <option key={option.ID} value={option.SFORNO}>
                            {option.SFORNO}
                          </option>
                        ))}
                      </TextField>
                    </Grid>
                    <Grid item XS={6} md={1}>
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
                    <Grid item XS={6} md={1}>
                      <Button
                        fullWidth
                        type="submit"
                        id="vSubmit"
                        variant="contained"
                        color="secondary"
                        onClick={(event) => {
                          values.vSubmit = "CREATE";
                          values.vStatus = "00";
                        }}
                      >
                        CREATE
                      </Button>
                    </Grid>
                    <Grid item XS={6} md={1}>
                      <Button
                        fullWidth
                        type="submit"
                        id="vSubmit"
                        variant="contained"
                        color="secondary"
                        onClick={(event) => {
                          values.vSubmit = "SAVE";
                          values.vStatus = "00";
                        }}
                      >
                        SAVE
                      </Button>
                    </Grid>
                  </Grid>

                  <Grid item xs={1} spacing={1}>
                    <TextField
                      fullWidth
                      width="20%"
                      size="small"
                      variant="outlined"
                      id="vReference"
                      label="Reference"
                      value={swrnumber.vReference}
                      values={(values.vReference = swrnumber.vReference)}
                      onChange={(event) => {
                        setswrnumber({
                          ...swrnumber,
                          vReference: event.target.value,
                        });
                      }}
                    ></TextField>
                  </Grid>

                  <Grid item xs={1} spacing={1}>
                    <TextField
                      fullWidth
                      width="20%"
                      size="small"
                      variant="outlined"
                      id="vName"
                      label="NAME"
                      value={swrnumber.vName}
                      values={(values.vName = swrnumber.vName)}
                      onChange={(event) => {
                        setswrnumber({
                          ...swrnumber,
                          vName: event.target.value,
                        });
                      }}
                    ></TextField>
                  </Grid>
                  <Grid item xs={1} spacing={1}>
                    <TextField
                      fullWidth
                      width="20%"
                      size="small"
                      variant="outlined"
                      id="vLine"
                      label="LINE"
                      value={swrnumber.vLine}
                      values={(values.vLine = swrnumber.vLine)}
                      onChange={(event) => {
                        setswrnumber({
                          ...swrnumber,
                          vLine: event.target.value,
                        });
                      }}
                    ></TextField>
                  </Grid>
                  <Grid item xs={1} spacing={1}>
                    <TextField
                      fullWidth
                      width="20%"
                      size="small"
                      variant="outlined"
                      id="vType"
                      label="TYPE"
                      value={swrnumber.vType}
                      values={(values.vType = swrnumber.vType)}
                      onChange={(event) => {
                        setswrnumber({
                          ...swrnumber,
                          vType: event.target.value,
                        });
                      }}
                    ></TextField>
                  </Grid>
                  <Grid item xs={1} spacing={1}>
                    <TextField
                      fullWidth
                      width="20%"
                      size="small"
                      variant="outlined"
                      id="vRemark1"
                      label="REMARK 1"
                      value={swrnumber.vRemark1}
                      values={(values.vRemark1 = swrnumber.vRemark1)}
                      onChange={(event) => {
                        setswrnumber({
                          ...swrnumber,
                          vRemark1: event.target.value,
                        });
                      }}
                    ></TextField>
                  </Grid>
                  <Grid item xs={1} spacing={1}>
                    <TextField
                      required
                      fullWidth
                      width="20%"
                      size="small"
                      variant="outlined"
                      id="vRemark2"
                      label="REMARK 2"
                      value={swrnumber.vRemark2}
                      values={(values.vRemark2 = swrnumber.vRemark2)}
                      onChange={(event) => {
                        setswrnumber({
                          ...swrnumber,
                          vRemark2: event.target.value,
                        });
                      }}
                    ></TextField>
                  </Grid>
                </Grid>
              </Grid>
              <br />
            </Paper>
          </Grid>
        </Grid>
      </form>
    );
  };

  const showHeaderNameForm = ({
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
                <Grid container item xs={12} spacing={1}>
                  <Grid container item xs={12} spacing={1}>
                    <Grid container item xs={6} spacing={1}>
                      <TextField
                        fullWidth
                        width="20%"
                        size="small"
                        variant="outlined"
                        id="vSwrname"
                        label="Software Name"
                        SelectProps={{
                          native: true,
                        }}
                        helperText="Please type software name"
                        value={swrheader.vOrderno}
                        values={(values.vOrderno = swrnumber.vOrderno)}
                        onChange={(event) => {
                          // todo

                          setswrheader({
                            ...swrheader,
                            vOrderno: event.target.value,
                          });
                        }}
                      >
                        <option />
                      </TextField>
                    </Grid>
                    <Grid container item xs={6} spacing={1}>
                      <h2>&nbsp; Current Version :　</h2>
                      <h2>&nbsp; {values.vOrderno}</h2>
                    </Grid>
                  </Grid>
                </Grid>
              </Grid>
              <br />
            </Paper>
          </Grid>
        </Grid>
      </form>
    );
  };

  return (
    <div className={classes.root}>
      {/* DialogFile */}

      <Formik
        initialValues={{
          vReference: "aa",
          vOrderno: "bb",
          vName: "cc",
          vLine: "dd",
          vType: "ee",
          vRemark1: "ff",
          vRemark2: "gg",
          vSubmit: "hh",
          vStatus: "ii",
        }}
        onSubmit={(values, { setSubmitting }) => {
          alert(JSON.stringify(values));
          let formData = new FormData();

          formData.append("vReference", values.vReference);
          formData.append("vOrderno", values.vOrderno);
          formData.append("vName", values.vName);
          formData.append("vLine", values.vLine);
          formData.append("vType", values.vType);
          formData.append("vRemark1", values.vRemark1);
          formData.append("vRemark2", values.vRemark2);

          (async function() {
            switch (values.vSubmit) {
              case "Search":
                alert("Search");
                await dispatch(swrfileActions.fetchSWRFile());
                break;
              case "CREATE":
                alert("CREATE");
                await dispatch(
                  swrfileActions.addSWRfile(formData, props.history)
                );
                await dispatch(swrfileActions.fetchSWRFile());
                break;
              default:
                await dispatch(swrfileActions.fetchSWRFile());
                break;
            }
          })();
        }}
      >
        {(props) => showHeaderNameForm(props)}
      </Formik>

      <Formik
        initialValues={{
          vReference: "aa",
          vOrderno: "bb",
          vName: "cc",
          vLine: "dd",
          vType: "ee",
          vRemark1: "ff",
          vRemark2: "gg",
          vSubmit: "hh",
          vStatus: "ii",
        }}
        onSubmit={(values, { setSubmitting }) => {
          alert(JSON.stringify(values));
          //alert("xxxxxx");
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
            switch (values.vSubmit) {
              case "Search":
                alert("Search");
                await dispatch(swrfileActions.fetchSWRFile());
                break;
              case "CREATE":
                alert("CREATE");
                await dispatch(
                  swrfileActions.addSWRfile(formData, props.history)
                );
                await dispatch(swrfileActions.fetchSWRFile());
                break;
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
