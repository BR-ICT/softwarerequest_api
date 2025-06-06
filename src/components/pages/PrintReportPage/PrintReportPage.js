import React, { useEffect, useState, useMemo } from "react";
import { useSelector, useDispatch } from "react-redux";
import moment from "moment";
import NumberFormat from "react-number-format";
import MaterialTable, { MTableToolbar } from "material-table";
import {
  makeStyles,
  withStyles,
  ThemeProvider,
  createMuiTheme,
} from "@material-ui/core/styles";
import { Typography, Grid, Paper, TextField, Button } from "@material-ui/core";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import DialogTitle from "@material-ui/core/DialogTitle";
import SearchIcon from "@material-ui/icons/Search";
import CancelIcon from "@material-ui/icons/Cancel";
import PrintIcon from "@material-ui/icons/Print";
import Autocomplete from "@material-ui/lab/Autocomplete";
import { Formik, Form, Field } from "formik";
import { red, green, purple } from "@material-ui/core/colors/";
import * as loginActions from "./../../../actions/login.action";
import * as adrnumberActions from "./../../../actions/adrnumber.action";

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
}));

export default (props) => {
  const classes = useStyles();
  const dispatch = useDispatch();
  const adrnumberReducer = useSelector(
    ({ adrnumberReducer }) => adrnumberReducer
  );

  const initialStateADRNumber = {
    vADRSelectNumber: null,
    vWarehouse: null,
    vDepartment: null,
    vCostCenter: null,
    vPHGroup: null,
    vMonth: null,
    vStatus: null,
  };
  const [adrnumber, setADRNumber] = useState(initialStateADRNumber);

  useEffect(() => {
    let fromStatus = "30";
    let toStatus = "30";
    dispatch(adrnumberActions.getADRNumber(fromStatus, toStatus));

    adrnumberReducer.result = null;
  }, []);

  const adrnumbers = useMemo(() =>
    adrnumberReducer.result ? adrnumberReducer.result : []
  );

  const showForm = ({
    values,
    handleChange,
    handleSubmit,
    setFieldValue,
    isSubmitting,
  }) => {
    return (
      <form onSubmit={handleSubmit}>
        <Grid container style={{ marginBottom: 2 }} spacing={5}>
          <Grid item xs={12}>
            <Paper className={classes.paper}>
              <Grid container item xs={12} className={classes.margin}>
                <Grid item xs={12} sm={2} className={classes.margin}>
                  <TextField
                    fullWidth
                    select
                    size="small"
                    variant="outlined"
                    // required
                    id="vSelectADRNumber"
                    label="ADR Number"
                    value={adrnumber.vPRSelectNumber}
                    onChange={(event) => {
                      // console.log(event.target.value);
                      setADRNumber({
                        ...adrnumber,
                        vADRSelectNumber:
                          event.target.value == "" ? null : event.target.value,
                      });
                    }}
                    InputLabelProps={{ shrink: true }}
                    SelectProps={{
                      native: true,
                    }}
                  >
                    <option />
                    {adrnumbers.map((option) => (
                      <option key={option.ID} value={option.ADRNUMBER}>
                        {option.ADRNUMBER}
                      </option>
                    ))}
                  </TextField>
                </Grid>

                <Grid className={classes.margin}>
                  <a
                    href={`${
                      process.env.REACT_APP_API_URL
                    }/adr_api/api_report/viewadr/${loginActions.getTokenCono()}/${loginActions.getTokenDivi()}/${
                      adrnumber.vADRSelectNumber
                    }`}
                    target="_blank"
                    style={{ textDecoration: "none" }}
                  >
                    <Button
                      fullWidth
                      variant="contained"
                      color="primary"
                      // disabled={viewMPRDisable}
                      startIcon={<SearchIcon />}
                    >
                      View ADR
                    </Button>
                  </a>
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
      {/* Grid */}
      <Formik>{(props) => showForm(props)}</Formik>
    </div>
  );
};
