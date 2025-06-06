import React, { useState } from "react";
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
import * as main_requestActions from "../../../actions/main_request.action";

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
  const [filedetail, setFileDetail] = useState(initialFileDetail);
  const dispatch = useDispatch();
  const classes = useStyles();
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
              <Grid container item xs={12} spacing={1}>
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
                      Send Request
                    </Button>
                  </Grid>
                </Grid>
              </Grid>
              <br />
              <MaterialTable></MaterialTable>
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
            await dispatch(
              main_requestActions.addMAIN_REQUEST(formData, props.history)
            );
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
