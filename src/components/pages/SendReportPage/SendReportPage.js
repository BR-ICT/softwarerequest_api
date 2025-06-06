import React, { useEffect, useState, useMemo, useRef } from "react";
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
import clsx from "clsx";
import {
  Typography,
  Grid,
  Paper,
  TextField,
  Button,
  Input,
} from "@material-ui/core";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import DialogTitle from "@material-ui/core/DialogTitle";
import Autocomplete from "@material-ui/lab/Autocomplete";
import SearchIcon from "@material-ui/icons/Search";
import AddIcon from "@material-ui/icons/Add";
import AddCircleIcon from "@material-ui/icons/AddCircle";
import SaveIcon from "@material-ui/icons/Save";
import CancelIcon from "@material-ui/icons/Cancel";
import DeleteIcon from "@material-ui/icons/Delete";
import SendIcon from "@material-ui/icons/Send";
import DoneIcon from "@material-ui/icons/Done";
import CircularProgress from "@material-ui/core/CircularProgress";
import { Formik, Form, Field } from "formik";
import {
  red,
  green,
  purple,
  teal,
  deepOrange,
  blueGrey,
} from "@material-ui/core/colors/";
import * as loginActions from "./../../../actions/login.action";
import * as adrnumberActions from "./../../../actions/adrnumber.action";
import * as adrheadActions from "./../../../actions/adrhead.action";
import * as adrdetailActions from "./../../../actions/adrdetail.action";
import * as buActions from "./../../../actions/bu.action";
import * as costcenterActions from "./../../../actions/costcenter.action";
import * as approveActions from "./../../../actions/approve.action";
import * as itemActions from "./../../../actions/item.action";
import * as accountantActions from "./../../../actions/accountant.action";

const useStyles = makeStyles((theme) => ({
  root: {
    width: "100%",
    marginTop: 60,
  },
  wrapper: {
    margin: theme.spacing(1),
    position: "relative",
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
    margin: theme.spacing(1),
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

export default (props) => {
  const classes = useStyles();
  const dispatch = useDispatch();
  const adrnumberReducer = useSelector(
    ({ adrnumberReducer }) => adrnumberReducer
  );
  const adrheadReducer = useSelector(({ adrheadReducer }) => adrheadReducer);
  const adrdetailReducer = useSelector(
    ({ adrdetailReducer }) => adrdetailReducer
  );

  const prnumberbuyerReducer = useSelector(
    ({ prnumberbuyerReducer }) => prnumberbuyerReducer
  );
  const accountantReducer = useSelector(
    ({ accountantReducer }) => accountantReducer
  );
  const buReducer = useSelector(({ buReducer }) => buReducer);
  const departmentReducer = useSelector(
    ({ departmentReducer }) => departmentReducer
  );
  const costcenterReducer = useSelector(
    ({ costcenterReducer }) => costcenterReducer
  );
  const approveReducer = useSelector(({ approveReducer }) => approveReducer);
  const itemReducer = useSelector(({ itemReducer }) => itemReducer);

  const [adrnumber, setADRNumber] = useState({
    vADRSelectNumber: "",
    vADRSelectNumberLine: "",
  });
  const initialStateADRHead = {
    vADRNumber: "",
    vDate: moment(new Date()).format("YYYY-MM-DD"),
    vMonth: moment(new Date()).format("YYYYMM"),
    vType: "",
    vPrefix: "",
    vBOI: "",
    vBU: "",
    vCostcenter: "",
    vCostcenterDesc: "",
    vVat: "",
    vAccountant: "",
    vRequestor: loginActions.getTokenUsername(),
    vRemark: "",
    vApprove1: "",
    vApprove2: "VILAI_DEC",
    vApprove3: "WEERAS_WAH",
    vApprove4: "ROSANN_SUC",
    vStatus: "",
    vImageFile: "",
    vImagePreview: "",
    vImagePreview2: "",
  };
  const [adrhead, setADRHead] = useState(initialStateADRHead);
  const initialStateItemADRDetail = {
    vItemLine: "",
    vItemNo: "",
    vItemDesc1: "",
    vItemDesc2: null,
    vSBNO: "",
    vItemCostcenter: "",
    vItemDate: "",
    vAssetCost: "",
    vNetValue: "",
    vItemQty: "",
    vItemPrice: "",
    vItemRemark: "",
    vImageFile: "",
    vImagePreview: "",
    vImagePreview2: "",
  };
  const [itemadrdetail, setItemADRDetail] = useState(initialStateItemADRDetail);

  const [searchdisable, setSearchDisable] = useState(false);
  const [newdisable, setNewDisable] = useState(false);
  const [editdisable, setEditDisable] = useState(true);
  const [canceldisable, setCancelDisable] = useState(true);
  const [savedisable, setSaveDisable] = useState(true);
  const [cleardisable, setClearDisable] = useState(true);
  const [submitdisable, setSubmitDisable] = useState(true);
  const [boireqdisable, setBOIReqDisable] = useState(true);
  const [imagereqdisable, setImageReqDisable] = useState(true);
  const [additemdisable, setAddItemDisable] = useState(true);
  const [editnamedisable, setEditNameDisable] = useState(true);
  const [create, setCreate] = useState(true);
  const [update, setUpdate] = useState(false);
  const [confirm, setConfirm] = useState(false);
  const [reject, setReject] = useState(false);
  const [openDialog, setOpenDialog] = useState(false);
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [loading, setLoading] = useState(false);
  const [loadingcancel, setLoadingCancel] = useState(false);
  const [success, setSuccess] = useState(false);
  const [file, setFile] = useState(false);
  const inputRef = useRef(null);

  useEffect(() => {
    let fromStatus = "30";
    let toStatus = "30";
    dispatch(adrnumberActions.getADRNumber(fromStatus, toStatus));
    dispatch(buActions.getBU());
    dispatch(accountantActions.getAccountant());
    dispatch(approveActions.getApproves());

    adrheadReducer.result = null;
    adrdetailReducer.result = null;
  }, []);

  useEffect(() => {
    const adrheads = adrheadReducer.result ? adrheadReducer.result : [];

    adrheads.map((item) => {
      // console.log("prheads.vStatus: " + item.HD_STATUS);
      dispatch(costcenterActions.getCostCentersBU(item.ADBU));

      setADRHead({
        ...adrhead,
        vADRNumber: item.ADRNUMBER,
        vDate: item.ADDATE,
        vMonth: item.ADMONT,
        vType: item.ADTYPE,
        vPrefix: item.ADPREF,
        vBOI: item.ADBOI,
        vBU: item.ADBU,
        vCostcenter: item.ADCOCE,
        vCostcenterDesc: item.COSTCENTER,
        vVat: item.ADVAT,
        vAccountant: item.ADACCT,
        vRequestor: item.ADREQU,
        vRemark: item.ADREM1,
        vApprove1: item.ADAPP1,
        vApprove2: item.ADAPP2,
        vApprove3: item.ADAPP3,
        vApprove4: item.ADAPP4,
        vStatus: item.ADSTAT,
        vImagePreview2: item.IMAGE,
      });

      // dispatch(
      //   itemActions.getItems(
      //     item.ADBU,
      //     moment(item.ADMONT)
      //       .add(-1, "months")
      //       .format("YYYYMM")
      //   )
      // );
    });
  }, [adrheadReducer]);

  const adrnumbers = useMemo(() =>
    adrnumberReducer.result ? adrnumberReducer.result : []
  );

  const accountants = useMemo(() =>
    accountantReducer.result ? accountantReducer.result : []
  );

  const bus = useMemo(() => (buReducer.result ? buReducer.result : []));

  const costcenters = useMemo(() =>
    costcenterReducer.result ? costcenterReducer.result : []
  );

  const approves = useMemo(() =>
    approveReducer.result ? approveReducer.result : []
  );

  const items = itemReducer.result ? itemReducer.result : [];

  const ColorButton = withStyles((theme) => ({
    root: {
      color: theme.palette.getContrastText(green[500]),
      backgroundColor: green[500],
      "&:hover": {
        backgroundColor: green[700],
      },
    },
  }))(Button);

  const ColorButtonTeal = withStyles((theme) => ({
    root: {
      color: theme.palette.getContrastText(teal[500]),
      backgroundColor: teal[500],
      "&:hover": {
        backgroundColor: teal[700],
      },
    },
  }))(Button);

  const ColorButtonGreen = withStyles((theme) => ({
    root: {
      color: theme.palette.getContrastText(green[500]),
      backgroundColor: green[500],
      "&:hover": {
        backgroundColor: green[700],
      },
    },
  }))(Button);

  const ColorButtonDeepOrange = withStyles((theme) => ({
    root: {
      color: theme.palette.getContrastText(deepOrange[500]),
      backgroundColor: deepOrange[500],
      "&:hover": {
        backgroundColor: deepOrange[700],
      },
    },
  }))(Button);

  const ColorButtonDeepBlueGray = withStyles((theme) => ({
    root: {
      color: theme.palette.getContrastText(blueGrey[500]),
      backgroundColor: blueGrey[500],
      "&:hover": {
        backgroundColor: blueGrey[700],
      },
    },
  }))(Button);

  const handleSearch = (adrnumber = "") => {
    if (adrnumber != "") {
      setCreate(false);
      setSearchDisable(false);
      setNewDisable(true);
      setSaveDisable(false);
      setClearDisable(false);
      setCancelDisable(false);
      setSubmitDisable(false);
      setEditDisable(false);

      setAddItemDisable(false);

      let fromStatus = "30";
      let toStatus = "30";
      dispatch(adrheadActions.getADRHead(adrnumber, fromStatus, toStatus));
      // dispatch(adrdetailActions.getADRDetail(adrnumber, fromStatus, toStatus));
    } else {
      setADRHead({
        ...initialStateADRHead,
      });
      adrheadReducer.result = null;
      adrdetailReducer.result = null;
    }
  };

  const handleNew = () => {
    // setOrderNumber(initialStateOrderNumber);
    setCreate(true);
    // setOrderDisable(true);
    setSearchDisable(true);
    setNewDisable(true);
    setSaveDisable(false);
    setClearDisable(false);
    setEditDisable(false);
  };

  const handleClear = () => {
    setADRNumber({ ...adrnumber, vADRSelectNumber: "" });
    setADRHead({ ...initialStateADRHead });
    setItemADRDetail({ ...initialStateItemADRDetail });
    adrheadReducer.result = null;
    adrdetailReducer.result = null;

    setCreate(true);
    setSearchDisable(false);
    setNewDisable(false);
    setSaveDisable(true);
    setClearDisable(true);
    setCancelDisable(true);
    setSubmitDisable(true);
    setEditDisable(true);
    setAddItemDisable(true);
  };

  const handleDialogClose = () => {
    setOpenDialog(false);
    setItemADRDetail(initialStateItemADRDetail);
    resetFileInput();
  };

  const resetFileInput = () => {
    // 👇️ reset input value
    inputRef.current.value = null;
    setFile(false);
  };

  const types = [
    {
      ID: "0",
      TYPE: "Sales",
    },
    {
      ID: "1",
      TYPE: "Destroy",
    },
    {
      ID: "2",
      TYPE: "Write off",
    },
  ];

  const bois = [
    {
      ID: "0",
      BOI: "Non BOI",
    },
    {
      ID: "1",
      BOI: "BOI",
    },
  ];

  const vats = [
    {
      ID: "0",
      VALUE: "0",
      VAT: "Non Vat",
    },
    {
      ID: "1",
      VALUE: "7",
      VAT: "Vat 7%",
    },
  ];

  const remarks = [
    {
      ID: "0",
      REMARK: "Damaged",
    },
    {
      ID: "1",
      REMARK: "Disappeared",
    },
    {
      ID: "2",
      REMARK: "Donation",
    },
  ];

  const showForm = ({
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
                <Grid item xs={12} md={3}>
                  <TextField
                    fullWidth
                    select
                    size="small"
                    variant="outlined"
                    id="vSelectADRNumber"
                    label="ADR Number"
                    disabled={searchdisable}
                    value={adrnumber.vADRSelectNumber}
                    onChange={(event) => {
                      // console.log(event.target.value);
                      if (event.target.value) {
                        setADRNumber({
                          ...adrnumber,
                          vADRSelectNumber: event.target.value,
                        });
                        handleSearch(event.target.value);
                      }
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
                <Grid item xs={6} md={"auto"}>
                  <Button
                    fullWidth
                    size="medium"
                    id="vSearch"
                    variant="contained"
                    color="primary"
                    disabled={searchdisable}
                    startIcon={<SearchIcon />}
                    onClick={(event) => {
                      handleSearch(adrnumber.vADRSelectNumber);
                    }}
                  >
                    Search
                  </Button>
                </Grid>
                {/* <Grid item xs={6} md={"auto"}>
                  <Button
                    fullWidth
                    size="medium"
                    id="vNew"
                    variant="contained"
                    color="secondary"
                    disabled={newdisable}
                    startIcon={<AddIcon />}
                    onClick={handleNew}
                  >
                    New
                  </Button>
                </Grid> */}
                {/* <Grid item xs={6} md={"auto"}>
                  <ColorButtonDeepOrange
                    fullWidth
                    size="medium"
                    id="vSave"
                    variant="contained"
                    color="primary"
                    type="submit"
                    startIcon={<SaveIcon />}
                    disabled={savedisable}
                    onClick={(event) => {
                      if (create) {
                        values.vSubmit = "create";
                        values.vStatus = "00";
                      } else {
                        values.vSubmit = "update";
                        // values.vStatus = "00";
                      }
                    }}
                  >
                    {create ? "Save" : "Update"}
                  </ColorButtonDeepOrange>
                </Grid> */}
                <Grid item xs={6} md={"auto"}>
                  <ColorButtonDeepBlueGray
                    fullWidth
                    size="medium"
                    id="vClear"
                    variant="contained"
                    color="secondary"
                    startIcon={<CancelIcon />}
                    disabled={cleardisable}
                    onClick={handleClear}
                  >
                    Clear
                  </ColorButtonDeepBlueGray>
                </Grid>
              </Grid>

              <Grid container item xs spacing={1}>
                <Grid item xs={6} md={"auto"}>
                  <TextField
                    className={classes.margin}
                    disabled={true}
                    fullWidth
                    required
                    size="small"
                    id="vADRNumber"
                    label="ADR Number"
                    placeholder="ADR Number"
                    variant="outlined"
                    value={adrhead.vADRNumber}
                    values={(values.vADRNumber = adrhead.vADRNumber)}
                    onChange={(event) => {
                      // console.log(event.target.value);
                      setADRHead({
                        ...adrhead,
                        vADRNumber: event.target.value,
                      });
                    }}
                    InputLabelProps={{ shrink: true }}
                  />
                </Grid>

                <Grid item xs={6} md={"auto"}>
                  <TextField
                    className={classes.margin}
                    disabled={true}
                    fullWidth
                    required
                    type="date"
                    size="small"
                    id="vDate"
                    label="Date"
                    variant="outlined"
                    defaultValue={adrhead.vDate}
                    value={adrhead.vDate}
                    values={(values.vDate = adrhead.vDate)}
                    onChange={(event) => {
                      // console.log("onChange: " + event.target.value.substr(2, 2) +
                      // event.target.value.substr(5, 2));
                      var dateNow = new Date();
                      if (
                        event.target.value <
                        moment(dateNow).format("YYYY-MM-DD")
                      ) {
                        alert("Date not less than present day.");
                      } else {
                        setADRHead({
                          ...adrhead,
                          vDate: event.target.value,
                          vMonth:
                            event.target.value.substr(2, 2) +
                            event.target.value.substr(5, 2),
                        });
                      }
                    }}
                    InputLabelProps={{ shrink: true, required: true }}
                  />
                </Grid>

                <Grid item xs={6} md={1}>
                  <TextField
                    className={classes.margin}
                    disabled={true}
                    fullWidth
                    required
                    size="small"
                    id="vMonth"
                    label="Month"
                    placeholder="Month"
                    variant="outlined"
                    value={adrhead.vMonth}
                    values={(values.vMonth = adrhead.vMonth)}
                    InputLabelProps={{ shrink: true }}
                  />
                </Grid>

                <Grid item xs={6} md={"auto"}>
                  <TextField
                    className={classes.margin}
                    disabled={create ? editdisable : true}
                    fullWidth
                    required
                    error
                    select
                    size="small"
                    variant="outlined"
                    id="vType"
                    label="Type"
                    value={adrhead.vType}
                    values={(values.vType = adrhead.vType)}
                    onChange={(event) => {
                      // console.log(event.target.value);

                      if (event.target.value === "0") {
                        setADRHead({
                          ...adrhead,
                          vType: event.target.value,
                          vPrefix: "S",
                          vRemark: "Damaged",
                        });
                        values.vPrefix = "S";
                        setBOIReqDisable(true);
                        setImageReqDisable(true);
                      } else if (event.target.value === "1") {
                        setADRHead({
                          ...adrhead,
                          vType: event.target.value,
                          vPrefix: "D",
                          vRemark: "Damaged",
                        });
                        values.vPrefix = "D";
                        setBOIReqDisable(true);
                        setImageReqDisable(true);
                      } else {
                        setADRHead({
                          ...adrhead,
                          vType: event.target.value,
                          vPrefix: "W",
                          vBOI: "",
                          vRemark: "Disappeared",
                        });
                        values.vPrefix = "W";
                        setBOIReqDisable(false);
                        setImageReqDisable(false);
                      }

                      // if (
                      //   event.target.value === "0" ||
                      //   event.target.value === "1"
                      // ) {
                      //   setADRHead({
                      //     ...adrhead,
                      //     vType: event.target.value,
                      //     vRemark: "Damaged",
                      //   });
                      //   setBOIReqDisable(true);
                      //   setImageReqDisable(true);
                      // } else {
                      //   setADRHead({
                      //     ...adrhead,
                      //     vType: event.target.value,
                      //     vBOI: "",
                      //     vRemark: "Disappeared",
                      //   });

                      //   setBOIReqDisable(false);
                      //   setImageReqDisable(false);
                      // }
                    }}
                    InputLabelProps={{ shrink: true, required: true }}
                    SelectProps={{
                      native: true,
                    }}
                  >
                    <option />
                    {types.map((option) => (
                      <option key={option.ID} value={option.ID}>
                        {option.TYPE}
                      </option>
                    ))}
                  </TextField>
                </Grid>

                <Grid item xs={6} md={"auto"}>
                  <TextField
                    className={classes.margin}
                    disabled={create ? editdisable : true}
                    fullWidth
                    required={boireqdisable}
                    error
                    select
                    size="small"
                    variant="outlined"
                    id="vBOI"
                    label="BOI"
                    value={adrhead.vBOI}
                    values={(values.vBOI = adrhead.vBOI)}
                    onChange={(event) => {
                      console.log(event.target.value);
                      setADRHead({
                        ...adrhead,
                        vBOI: event.target.value,
                      });
                    }}
                    InputLabelProps={{ shrink: true }}
                    SelectProps={{
                      native: true,
                    }}
                  >
                    <option />
                    {bois.map((option) => (
                      <option key={option.ID} value={option.ID}>
                        {option.BOI}
                      </option>
                    ))}
                  </TextField>
                </Grid>

                <Grid item xs={6} md={2}>
                  <TextField
                    className={classes.margin}
                    disabled={create ? editdisable : true}
                    fullWidth
                    required
                    error
                    select
                    size="small"
                    id="vBU"
                    label="BU"
                    placeholder="BU"
                    variant="outlined"
                    value={adrhead.vBU}
                    values={(values.vBU = adrhead.vBU)}
                    onChange={(event) => {
                      // console.log(event.target.value);
                      setADRHead({
                        ...adrhead,
                        vBU: event.target.value,
                      });

                      dispatch(
                        costcenterActions.getCostCentersBU(event.target.value)
                      );

                      dispatch(
                        itemActions.getItems(
                          event.target.value,
                          moment(adrhead.vMonth)
                            .add(-1, "months")
                            .format("YYYYMM")
                        )
                      );
                    }}
                    InputLabelProps={{ shrink: true, required: true }}
                    SelectProps={{
                      native: true,
                    }}
                  >
                    <option />
                    {bus.map((option) => (
                      <option key={option.ID} value={option.S1STID}>
                        {option.BU}
                      </option>
                    ))}
                  </TextField>
                </Grid>

                <Grid item xs={6} md={2}>
                  <TextField
                    className={classes.margin}
                    disabled={create ? editdisable : true}
                    fullWidth
                    required
                    error
                    select
                    size="small"
                    id="vCostcenter"
                    label="Costcenter"
                    placeholder="Placeholder"
                    variant="outlined"
                    value={adrhead.vCostcenterDesc}
                    values={(values.vCostcenter = adrhead.vCostcenter)}
                    onChange={(event) => {
                      // console.log(event.target.value);
                      if (event.target.value) {
                        let getFullCostcenter;
                        let getCostCenter;
                        let getVatclaim;

                        getFullCostcenter = event.target.value.split(":");
                        getCostCenter = getFullCostcenter[0].trim();
                        getVatclaim = event.target.value.slice(-3);

                        // console.log("getVatclaim: " + getVatclaim);
                        if (getVatclaim === "UCN" || getVatclaim === "UCP") {
                          setADRHead({
                            ...adrhead,
                            vCostcenter: getCostCenter,
                            vCostcenterDesc: event.target.value,
                            vVat: "7",
                          });
                        } else {
                          setADRHead({
                            ...adrhead,
                            vCostcenter: getCostCenter,
                            vCostcenterDesc: event.target.value,
                            vVat: "0",
                          });
                        }
                      }
                    }}
                    InputLabelProps={{ shrink: true, required: true }}
                    SelectProps={{
                      native: true,
                    }}
                  >
                    <option />
                    {costcenters.map((option) => (
                      <option key={option.ID} value={option.COSTCENTER}>
                        {option.COSTCENTER}
                      </option>
                    ))}
                  </TextField>
                </Grid>

                <Grid item xs={6} md={1}>
                  <TextField
                    className={classes.margin}
                    disabled={true}
                    fullWidth
                    error
                    select
                    size="small"
                    id="vVat"
                    label="Vat"
                    placeholder="Vat"
                    variant="outlined"
                    value={adrhead.vVat}
                    values={(values.vVat = adrhead.vVat)}
                    onChange={(event) => {
                      // console.log(event.target.value);
                      setADRHead({
                        ...adrhead,
                        vVat: event.target.value,
                      });
                    }}
                    InputLabelProps={{ shrink: true }}
                    SelectProps={{
                      native: true,
                    }}
                  >
                    <option />
                    {vats.map((option) => (
                      <option key={option.ID} value={option.VALUE}>
                        {option.VAT}
                      </option>
                    ))}
                  </TextField>
                </Grid>

                <Grid item xs={6} md={2}>
                  <TextField
                    className={classes.margin}
                    disabled={create ? editdisable : true}
                    fullWidth
                    required
                    error
                    select
                    size="small"
                    id="vAccountant"
                    label="Accountant"
                    placeholder="Accountant"
                    variant="outlined"
                    value={adrhead.vAccountant}
                    values={(values.vAccountant = adrhead.vAccountant)}
                    onChange={(event) => {
                      // console.log(event.target.value);
                      setADRHead({
                        ...adrhead,
                        vAccountant: event.target.value,
                      });
                    }}
                    InputLabelProps={{ shrink: true }}
                    SelectProps={{
                      native: true,
                    }}
                  >
                    <option />
                    {accountants.map((option) => (
                      <option key={option.ID} value={option.ACCOUNTANT}>
                        {option.ACCOUNTANT}
                      </option>
                    ))}
                  </TextField>
                </Grid>

                <Grid item xs={6} md={2}>
                  <TextField
                    className={classes.margin}
                    disabled={true}
                    fullWidth
                    required
                    size="small"
                    id="vRequestor"
                    label="Requestor"
                    placeholder="Requestor"
                    variant="outlined"
                    value={adrhead.vRequestor}
                    values={(values.vRequestor = adrhead.vRequestor)}
                    InputLabelProps={{ shrink: true, required: true }}
                  />
                </Grid>
              </Grid>

              <Grid container item xs={12} spacing={1}>
                <Grid item xs={12} md={4}>
                  <TextField
                    className={classes.margin}
                    disabled={create ? editdisable : true}
                    fullWidth
                    required
                    error
                    select
                    size="small"
                    id="vRemark"
                    label="Remark"
                    placeholder="Remark"
                    variant="outlined"
                    value={adrhead.vRemark}
                    values={(values.vRemark = adrhead.vRemark)}
                    onChange={(event) => {
                      // console.log(event.target.value);
                      setADRHead({
                        ...adrhead,
                        vRemark: event.target.value,
                      });

                      if (
                        adrhead.vType === "2" &&
                        event.target.value === "Disappeared"
                      ) {
                        setImageReqDisable(false);
                      } else {
                        setImageReqDisable(true);
                      }
                    }}
                    InputLabelProps={{ shrink: true }}
                    SelectProps={{
                      native: true,
                    }}
                  >
                    <option />
                    {remarks.map((option) => (
                      <option key={option.ID} value={option.REMARK}>
                        {option.REMARK}
                      </option>
                    ))}
                  </TextField>
                </Grid>
              </Grid>

              <Grid container item xs spacing={1}>
                <Grid item xs={6} md={2}>
                  <TextField
                    className={classes.margin}
                    // style={{ width: "200px" }}
                    error={true}
                    disabled={create ? editdisable : true}
                    fullWidth
                    select
                    size="small"
                    variant="outlined"
                    margin="normal"
                    required
                    id="vDeptHead"
                    label="Dept of Head"
                    value={adrhead.vApprove1}
                    values={(values.vApprove1 = adrhead.vApprove1)}
                    onChange={(event) => {
                      // console.log(event.target.value);
                      setADRHead({
                        ...adrhead,
                        vApprove1: event.target.value,
                      });
                    }}
                    InputLabelProps={{ shrink: true }}
                    SelectProps={{
                      native: true,
                    }}
                  >
                    <option />
                    {approves.map((option) => (
                      <option key={option.ID} value={option.US_LOGIN}>
                        {option.US_LOGIN}
                      </option>
                    ))}
                  </TextField>
                </Grid>
                <Grid item xs={6} md={2}>
                  <TextField
                    className={classes.margin}
                    // style={{ width: "200px" }}
                    // error={true}
                    disabled={true}
                    fullWidth
                    select
                    size="small"
                    variant="outlined"
                    margin="normal"
                    required
                    id="vApprove1"
                    label="Approve1"
                    value={adrhead.vApprove2}
                    values={(values.vApprove2 = adrhead.vApprove2)}
                    onChange={(event) => {
                      // console.log(event.target.value);
                      setADRHead({
                        ...adrhead,
                        vApprove2: event.target.value,
                      });
                    }}
                    InputLabelProps={{ shrink: true }}
                    SelectProps={{
                      native: true,
                    }}
                  >
                    <option />
                    {approves.map((option) => (
                      <option key={option.ID} value={option.US_LOGIN}>
                        {option.US_LOGIN}
                      </option>
                    ))}
                  </TextField>
                </Grid>
                <Grid item xs={6} md={2}>
                  <TextField
                    className={classes.margin}
                    // style={{ width: "200px" }}
                    // error={true}
                    disabled={true}
                    fullWidth
                    select
                    size="small"
                    variant="outlined"
                    margin="normal"
                    // required
                    id="vApprove2"
                    label="Approve2"
                    value={adrhead.vApprove3}
                    values={(values.vApprove3 = adrhead.vApprove3)}
                    onChange={(event) => {
                      // console.log(event.target.value);
                      setADRHead({
                        ...adrhead,
                        vApprove3: event.target.value,
                      });
                    }}
                    InputLabelProps={{ shrink: true }}
                    SelectProps={{
                      native: true,
                    }}
                  >
                    <option />
                    {approves.map((option) => (
                      <option key={option.ID} value={option.US_LOGIN}>
                        {option.US_LOGIN}
                      </option>
                    ))}
                  </TextField>
                </Grid>
                <Grid item xs={6} md={2}>
                  <TextField
                    className={classes.margin}
                    // style={{ width: "200px" }}
                    // error={true}
                    disabled={true}
                    fullWidth
                    select
                    size="small"
                    variant="outlined"
                    margin="normal"
                    // required
                    id="vApprove3"
                    label="Approve3"
                    value={adrhead.vApprove4}
                    values={(values.vApprove4 = adrhead.vApprove4)}
                    onChange={(event) => {
                      // console.log(event.target.value);
                      setADRHead({
                        ...adrhead,
                        vApprove4: event.target.value,
                      });
                    }}
                    InputLabelProps={{ shrink: true }}
                    SelectProps={{
                      native: true,
                    }}
                  >
                    <option />
                    {approves.map((option) => (
                      <option key={option.ID} value={option.US_LOGIN}>
                        {option.US_LOGIN}
                      </option>
                    ))}
                  </TextField>
                </Grid>
                {/* <Grid
                  item
                  xs={6}
                  md={"auto"}
                  className={(classes.margin, classes.wrapper)}
                >
                  <Button
                    // className={classes.margin}
                    fullWidth
                    size="medium"
                    id="vCancelADR"
                    variant="contained"
                    color="secondary"
                    type="submit"
                    // style={{ backgroundColor: green[500] }}
                    startIcon={<DeleteIcon />}
                    disabled={canceldisable}
                    onClick={(event) => {
                      values.vSubmit = "cancel";
                      values.vStatus = "99";
                    }}
                  >
                    Cancel ADR
                  </Button>
                  {loadingcancel && (
                    <CircularProgress
                      size={24}
                      className={classes.buttonProgress}
                    />
                  )}
                </Grid> */}
                <Grid
                  item
                  xs={6}
                  md={"auto"}
                  className={(classes.margin, classes.wrapper)}
                >
                  <ColorButton
                    // className={classes.margin}
                    fullWidth
                    size="medium"
                    id="vSubmitPH"
                    variant="contained"
                    color="secondary"
                    type="submit"
                    startIcon={<SendIcon />}
                    disabled={submitdisable}
                    onClick={(event) => {
                      values.vSubmit = "submit";
                      values.vStatus = "35";
                    }}
                  >
                    Submit
                  </ColorButton>
                  {loading && (
                    <CircularProgress
                      size={24}
                      className={classes.buttonProgress}
                    />
                  )}
                </Grid>
              </Grid>
            </Paper>
          </Grid>
        </Grid>
      </form>
    );
  };

  const showDetail = ({
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
                {/* <Grid item xs={12} md={"auto"}> */}
                <Button
                  fullWidth
                  disabled={additemdisable}
                  variant="contained"
                  color="primary"
                  startIcon={<AddCircleIcon />}
                  onClick={(event, rowData) => {
                    setSelectedProduct("rowData");
                    setOpenDialog(true);
                  }}
                >
                  Add Report
                </Button>
                {/* </Grid> */}
              </Grid>
              <Grid container item xs={12} spacing={1}>
                <Grid item xs={12} md={"auto"}>
                  <img
                    className={classes.margin}
                    width="100%"
                    height="80%"
                    src={`data:image/png;base64,${adrhead.vImagePreview2}`}
                  />
                </Grid>
              </Grid>
            </Paper>
          </Grid>
        </Grid>
      </form>
    );
  };

  const showDialog = ({
    values,
    handleChange,
    handleSubmit,
    setFieldValue,
    isSubmitting,
  }) => {
    if (selectedProduct === null) {
      return "";
    }

    return (
      <Dialog
        open={openDialog}
        keepMounted
        onClose={() => {}}
        aria-labelledby="alert-dialog-slide-title"
        aria-describedby="alert-dialog-slide-description"
      >
        <form onSubmit={handleSubmit}>
          <DialogTitle id="alert-dialog-slide-title">
            ADR Number : {adrhead.vADRNumber}
          </DialogTitle>
          <DialogContent>
            <TextField
              fullWidth
              required={imagereqdisable}
              inputRef={inputRef}
              id="vImage"
              label="Image: jpeg, png, gif"
              type="file"
              // value={itemadrdetail.vImageFile}
              values={(values.vImageFile = itemadrdetail.vImageFile)}
              InputLabelProps={{ shrink: true }}
              inputProps={{ accept: "image/png, image/gif, image/jpeg" }}
              onChange={(event) => {
                // console.log(event.target.files);
                if (event.target.files[0]) {
                  const file = event.target.files[0];
                  const reader = new FileReader();
                  reader.readAsDataURL(file);
                  reader.onload = () => {
                    // console.log("called: ", reader.result);
                    let base64String = reader.result
                      .replace("data:", "")
                      .replace(/^.+,/, "");

                    // console.log("base64String: ", base64String);
                    setItemADRDetail({
                      ...itemadrdetail,
                      vImageFile: file,
                      // vImagePreview: URL.createObjectURL(file),
                      vImagePreview2: base64String,
                    });

                    setFile(true);

                    values.vImageName =
                      adrhead.vADRNumber +
                      "_" +
                      itemadrdetail.vItemLine +
                      ".png";
                  };
                }
              }}
            />

            <Grid item xs>
              <img
                className={classes.margin}
                width="100%"
                height="80%"
                src={`data:image/png;base64,${itemadrdetail.vImagePreview2}`}
              />
            </Grid>
          </DialogContent>

          <DialogActions>
            <div>
              <Button onClick={handleDialogClose} color="default">
                Close
              </Button>
              <Button
                id="vSaveItem"
                type="submit"
                color="primary"
                onClick={(event) => {
                  if (itemadrdetail.vItemLine === "") {
                    values.vSubmit = "create";
                    values.vStatus = "00";
                  } else {
                    values.vSubmit = "update";
                    values.vStatus = "00";
                  }
                }}
              >
                {itemadrdetail.vItemLine === "" ? "Save" : "Update"}
              </Button>
              {loading && (
                <CircularProgress
                  size={24}
                  className={classes.buttonProgress}
                />
              )}
            </div>
          </DialogActions>
        </form>
      </Dialog>
    );
  };

  const columns = [
    {
      title: "Image",
      field: "IMAGE",
      editable: "never",
      headerStyle: { maxWidth: 50, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.IMAGE.length > 0 ? <DoneIcon /> : ""}
        </Typography>
      ),
    },
    {
      title: "Line",
      field: "ALLINE",
      // type: "numeric",
      editable: "never",
      width: 50,
      headerStyle: {
        maxWidth: 50,
        whiteSpace: "nowrap",
        textAlign: "center",
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
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.ALLINE}
        </Typography>
      ),
    },
    {
      title: "Item No",
      field: "ALITNO",
      editable: "never",
      headerStyle: { maxWidth: 150, whiteSpace: "nowrap", textAlign: "center" },
      cellStyle: {
        textAlign: "left",
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
      // editComponent: props => (
      //   <Autocomplete
      //     id="combo-box-demo"
      //     options={top100Films}
      //     getOptionLabel={option => option.title}
      //     style={{ width: 100 }}
      //     renderInput={params => <TextField {...params} />}
      //   />
      // ),
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.ALITNO}
        </Typography>
      ),
    },
    {
      title: "Item Name",
      field: "ALITDE",
      editable: "never",
      headerStyle: { maxWidth: 150, whiteSpace: "nowrap", textAlign: "center" },
      cellStyle: {
        textAlign: "left",
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
          {item.ALITDE}
        </Typography>
      ),
    },
    {
      title: "SBNO",
      field: "ALSBNO",
      editable: "never",
      headerStyle: { maxWidth: 50, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.ALSBNO}
        </Typography>
      ),
    },
    {
      title: "Cost Center",
      field: "ALCOST",
      editable: "never",
      headerStyle: { maxWidth: 50, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.ALCOST}
        </Typography>
      ),
    },
    {
      title: "Date",
      field: "ALDATE",
      // editable: "never",
      type: "date",
      headerStyle: { maxWidth: 150, whiteSpace: "nowrap", textAlign: "center" },
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
          {moment(item.ALDATE).format("DD/MM/YYYY")}
        </Typography>
      ),
    },
    {
      title: "Asset Cost",
      field: "ALACOS",
      editable: "never",
      headerStyle: { maxWidth: 50, whiteSpace: "nowrap", textAlign: "center" },
      cellStyle: {
        textAlign: "right",
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
          <NumberFormat
            value={item.ALACOS}
            displayType={"text"}
            thousandSeparator={true}
            // prefix={"$"}
          />
        </Typography>
      ),
    },
    {
      title: "Net Value",
      field: "ALNETV",
      editable: "never",
      headerStyle: { maxWidth: 50, whiteSpace: "nowrap", textAlign: "center" },
      cellStyle: {
        textAlign: "right",
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
          <NumberFormat
            value={item.ALNETV}
            displayType={"text"}
            thousandSeparator={true}
            // prefix={"$"}
          />
        </Typography>
      ),
    },

    {
      title: "Qty",
      field: "ALQTY",
      editable: "never",
      type: "numeric",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
      cellStyle: {
        textAlign: "right",
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
          {/* {item.PR_IBORQA} */}
          {/* var NumberFormat = require('react-number-format'); */}
          <NumberFormat
            value={item.ALQTY}
            displayType={"text"}
            thousandSeparator={true}
            // prefix={"$"}
          />
        </Typography>
      ),
    },
    {
      title: "Price",
      field: "ALPRIC",
      editable: "never",
      // type: "numeric",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
      cellStyle: {
        textAlign: "right",
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
          <NumberFormat
            value={item.ALPRIC}
            displayType={"text"}
            thousandSeparator={true}
            // prefix={"$"}
          />
        </Typography>
      ),
    },

    {
      title: "Remark",
      field: "ALREM1",
      editable: "never",
      headerStyle: { maxWidth: 150, whiteSpace: "nowrap", textAlign: "center" },
      cellStyle: {
        textAlign: "left",
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
          {item.ALREM1}
        </Typography>
      ),
    },
  ];

  return (
    <div className={classes.root}>
      {/* Grid */}
      {/* <p>#Debug ADRNumber {JSON.stringify(adrnumber)}</p> */}
      {/* <p>#Debug Head {JSON.stringify(adrhead)}</p> */}
      <Formik
        initialValues={{
          vADRNumber: "",
          vDate: "",
          vMonth: "",
          vType: "",
          vBOI: "",
          vBU: "",
          vCostcenter: "",
          vVat: "",
          vAccountant: "",
          vRequestor: "",
          vRemark: "",
          vApprove1: "",
          vApprove2: "",
          vApprove3: "",
          vApprove4: "",
          vStatus: "",
          vSubmit: "",
        }}
        onSubmit={(values, { setSubmitting }) => {
          // alert(JSON.stringify(values));
          let formData = new FormData();
          formData.append("vADRNumber", values.vADRNumber);
          formData.append("vDate", values.vDate);
          formData.append("vMonth", values.vMonth);
          formData.append("vType", values.vType);
          formData.append("vType", values.vType);
          formData.append("vPrefix", values.vPrefix);
          formData.append("vBOI", values.vBOI);
          formData.append("vBU", values.vBU);
          formData.append("vCostcenter", values.vCostcenter);
          formData.append("vVat", values.vVat);
          formData.append("vAccountant", values.vAccountant);
          formData.append("vRequestor", values.vRequestor);
          formData.append("vRemark", values.vRemark);
          formData.append("vApprove1", values.vApprove1);
          formData.append("vApprove2", values.vApprove2);
          formData.append("vApprove3", values.vApprove3);
          formData.append("vApprove4", values.vApprove4);
          formData.append("vStatus", values.vStatus);
          formData.append("vSubmit", values.vSubmit);

          formData.append("vAccRemark", "");
          formData.append("vAppRemark1", "");
          formData.append("vAppRemark2", "");
          formData.append("vAppRemark3", "");
          formData.append("vAppRemark4", "");

          if (values.vSubmit === "create") {
            (async function() {
              let data = await dispatch(adrheadActions.addADRHeadV2(formData));
              // console.log(data.message);
              alert(JSON.stringify(data));

              let fromStatus = "30";
              let toStatus = "30";
              await dispatch(
                adrnumberActions.getADRNumber(fromStatus, toStatus)
              );

              setADRNumber({ ...adrnumber, vADRSelectNumber: data.message });
              handleSearch(data.message);
            })();
          }

          if (values.vSubmit === "update") {
            (async function() {
              let data = await dispatch(adrheadActions.updateADRHead(formData));
              // console.log(data.message);
              alert(JSON.stringify(data));
              // setADRNumber({ ...adrnumber, vADRSelectNumber: data.message });
              // handleSearch(data.message);
            })();
          }

          if (values.vSubmit === "submit") {
            setSubmitDisable(true);
            setCancelDisable(true);
            setSuccess(false);
            setLoading(true);

            (async function() {
              let data = await dispatch(
                adrheadActions.updateStsADRHead(
                  values.vADRNumber,
                  values.vStatus,
                  values.vSubmit
                )
              );
              // console.log(data.message);
              alert(JSON.stringify(data));

              let fromStatus = "30";
              let toStatus = "30";
              await dispatch(
                adrnumberActions.getADRNumber(fromStatus, toStatus)
              );
              handleClear();

              setSuccess(true);
              setLoading(false);
            })();
          }
        }}
      >
        {(props) => showForm(props)}
      </Formik>

      <Formik
        initialValues={{}}
        onSubmit={(values, { setSubmitting }) => {
          // alert(JSON.stringify(values));
          // let formData = new FormData();
        }}
      >
        {(props) => showDetail(props)}
      </Formik>

      {/* Dialog */}
      <Formik
        initialValues={{
          vADRNumber: "",
          // vItemLine: "",
          // vItemNo: "",
          // vItemDesc1: "",
          // vItemDesc2: null,
          // vSBNO: "",
          // vItemCostcenter: "",
          // vItemDate: "",
          // vAssetCost: "",
          // vNetValue: "",
          // vItemQty: "",
          // vItemPrice: "",
          // vItemRemark: "",
          vImageFile: "",
          vImageName: "",
          vImagePreview: "",
          vImagePreview2: "",
          vStatus: "",
          vSubmit: "",
        }}
        onSubmit={(values, { setSubmitting }) => {
          // alert(JSON.stringify(values));
          let formData = new FormData();
          formData.append("vADRNumber", adrhead.vADRNumber);
          // formData.append("vPrefix", adrhead.vPrefix);
          // formData.append("vItemLine", itemadrdetail.vItemLine);
          // formData.append("vItemNo", values.vItemNo);
          // formData.append("vSBNO", values.vSBNO);
          // formData.append("vItemCostcenter", values.vItemCostcenter);
          // formData.append("vItemDate", values.vItemDate);
          // formData.append("vAssetCost", values.vAssetCost);
          // formData.append("vNetValue", values.vNetValue);
          // formData.append("vItemQty", values.vItemQty);
          // formData.append("vItemPrice", values.vItemPrice);
          // formData.append("vItemRemark", values.vItemRemark);

          if (file) {
            formData.append("vImageFile", values.vImageFile);
            formData.append("vImageName", values.vImageName);
          } else {
            formData.append("vImageFile", null);
            formData.append("vImageName", null);
          }

          formData.append("vStatus", values.vStatus);
          formData.append("vSubmit", values.vSubmit);

          if (values.vSubmit === "create") {
            (async function() {
              await dispatch(
                adrheadActions.updateADRImage(formData, props.history)
              );

              let fromStatus = "30";
              let toStatus = "30";
              dispatch(
                adrheadActions.getADRHead(
                  adrnumber.vADRSelectNumber,
                  fromStatus,
                  toStatus
                )
              );

              handleDialogClose();
            })();
          }
        }}
      >
        {(props) => showDialog(props)}
      </Formik>
    </div>
  );
};
