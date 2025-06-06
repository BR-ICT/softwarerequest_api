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
import { Typography, Grid, Paper, TextField, Button } from "@material-ui/core";
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
import * as prnumberbuyerActions from "./../../../actions/prnumberbuyer.action";
import * as prheadActions from "./../../../actions/prhead.action";
import * as prdetailbuyerActions from "./../../../actions/prdetailbuyer.action";
import * as warehouseActions from "./../../../actions/warehouse.action";
import * as buActions from "./../../../actions/bu.action";
import * as departmentActions from "./../../../actions/department.action";
import * as costcenterActions from "./../../../actions/costcenter.action";
import * as approveActions from "./../../../actions/approve.action";
import * as buyerActions from "./../../../actions/buyer.action";
import * as itemActions from "./../../../actions/item.action";
import * as itemunitActions from "./../../../actions/itemunit.action";
import * as phgroupActions from "./../../../actions/phgroup.action";
import * as phbuyerActions from "./../../../actions/phbuyer.action";
import * as supplierActions from "./../../../actions/supplier.action";
import * as prconfirmbuyerActions from "./../../../actions/prconfirmbuyer.action";
import * as genpoActions from "./../../../actions/genpo.action";
import * as deliveryActions from "./../../../actions/delivery.action";
import * as paymentActions from "./../../../actions/payment.action";
import { Alert } from "@material-ui/lab";

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
  const prnumberbuyerReducer = useSelector(
    ({ prnumberbuyerReducer }) => prnumberbuyerReducer
  );
  const prheadReducer = useSelector(({ prheadReducer }) => prheadReducer);
  const prdetailbuyerReducer = useSelector(
    ({ prdetailbuyerReducer }) => prdetailbuyerReducer
  );
  const warehouseReducer = useSelector(
    ({ warehouseReducer }) => warehouseReducer
  );
  const buReducer = useSelector(({ buReducer }) => buReducer);
  const departmentReducer = useSelector(
    ({ departmentReducer }) => departmentReducer
  );
  const costcenterReducer = useSelector(
    ({ costcenterReducer }) => costcenterReducer
  );
  const approveReducer = useSelector(({ approveReducer }) => approveReducer);
  const buyerReducer = useSelector(({ buyerReducer }) => buyerReducer);
  const itemReducer = useSelector(({ itemReducer }) => itemReducer);
  const itemunitReducer = useSelector(({ itemunitReducer }) => itemunitReducer);
  const phgroupReducer = useSelector(({ phgroupReducer }) => phgroupReducer);
  const phbuyerReducer = useSelector(({ phbuyerReducer }) => phbuyerReducer);
  const supplierReducer = useSelector(({ supplierReducer }) => supplierReducer);
  const genpoReducer = useSelector(({ genpoReducer }) => genpoReducer);
  const deliveryReducer = useSelector(({ deliveryReducer }) => deliveryReducer);
  const paymentReducer = useSelector(({ paymentReducer }) => paymentReducer);
  const prconfirmbuyerReducer = useSelector(
    ({ prconfirmbuyerReducer }) => prconfirmbuyerReducer
  );
  const [prnumber, setPRNumber] = useState({
    vPRSelectNumber: "",
    vPRSelectNumberLine: "",
  });
  const [ponumber, setPONumber] = useState({ vPOSelectNumber: "" });
  const initialStateADRHead = {
    vPRNumber: "",
    vDate: moment(new Date()).format("YYYY-MM-DD"),
    vWarehouse: "",
    vCostcenter: "",
    vMonth: moment(new Date()).format("YYMM"),
    vPlanUnPlan: "",
    vBU: "",
    vBuyer: "",
    vGroup: "",
    vCAPNo: "",
    vRequestor: "",
    vRemark: "",
    vApprove1: "",
    vApprove2: "",
    vApprove3: "",
    vApprove4: "",
    vStatus: "",
    vDelivery: "",
    vPayment: "",
  };
  const [adrhead, setADRHead] = useState(initialStateADRHead);
  const initialStateItemPRDetail = {
    vItemLine: "",
    vItemNo: "",
    vItemDesc1: "",
    vItemDesc2: null,
    vQty: "",
    vUnit: "",
    vDateDetail: moment(new Date()).format("YYYY-MM-DD"), //"2018-12-01"
    vSupplierNo: "",
    vSupplierName: "",
    vSupplierDesc: null,
    vPrice: "",
    vVat: "",
    vCurrency: "",
    vOrdertype: "",
    vTotal: "",
    vCostcenterDetail: "",
    vPHGroupDetail: "",
    vBuyerDetail: "",
    vRemarkDetail: "",
    vAddFreeItem: "",
    vImageFile: "",
    vImagePreview: null,
    vImagePreview2: null,
  };
  const [itemprdetail, setItemPRDetail] = useState(initialStateItemPRDetail);
  const [searchdisable, setSearchDisable] = useState(false);
  const [newdisable, setNewDisable] = useState(false);
  const [editdisable, setEditDisable] = useState(true);
  const [genpodisable, setGenPODisable] = useState(true);
  const [createdisable, setCreateDisable] = useState(true);
  const [cancelprdisable, setCancelPRDisable] = useState(true);
  const [savedisable, setSaveDisable] = useState(true);
  const [cleardisable, setClearDisable] = useState(true);
  const [confirmdisable, setConfirmDisable] = useState(false);
  const [addfreeitem, setAddFreeItem] = useState(false);
  const [editnamedisable, setEditNameDisable] = useState(true);
  const [deliverydisable, setDeliveryDisable] = useState(true);
  const [create, setCreate] = useState(false);
  const [update, setUpdate] = useState(false);
  const [confirm, setConfirm] = useState(false);
  const [reject, setReject] = useState(false);
  const [whsdisable, setWhsDisable] = useState(true);
  const [deptdisable, setDeptDisable] = useState(true);
  const [openDialog, setOpenDialog] = useState(false);
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [prconfirmbuyer, setPRConfirmBuyer] = useState(null);
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const timer = useRef();

  const [file, setFile] = useState();
  const [isUploading, setIsUploading] = useState();

  useEffect(() => {
    // console.log("dispatch prnumberbuyerActions");
    let status = "92";
    // dispatch(prnumberbuyerActions.getEPRNumbersGenPO(status));
    dispatch(warehouseActions.getWarehouses());
    dispatch(buActions.getBUs());
    // dispatch(approveActions.getApproves());
    // dispatch(buyerActions.getBuyers());
    // dispatch(supplierActions.getSuppliers());
    // dispatch(deliveryActions.getDeliverys());
    // dispatch(paymentActions.getPayments());
    // console.log(loginActions.getTokenUsername());
    // loginActions.getTokenUsername();
    prheadReducer.result = null;
    prdetailbuyerReducer.result = null;
  }, []);

  useEffect(() => {
    const prheads = prheadReducer.result ? prheadReducer.result : [];

    prheads.map((item) => {
      // console.log("prheads.vStatus: " + item.HD_STATUS);
      // if (item.HD_STATUS === "10") {
      dispatch(itemActions.getItems(item.HD_IBWHLO));
      let phgroup = "PH";
      let bu = item.HD_BU;
      let department = item.HD_IBCOCE;
      dispatch(phgroupActions.getPHGroups(phgroup));
      dispatch(costcenterActions.getCostCenters(department));
      dispatch(departmentActions.getDepartments(bu));
      setPRNumber({ ...prnumber, vPRSelectNumber: item.HD_IBPLPN });
      setADRHead({
        ...adrhead,
        vPRNumber: item.HD_IBPLPN,
        vDate: moment(item.HD_PURCDT).format("YYYY-MM-DD"),
        vWarehouse: item.HD_IBWHLO,
        vCostcenter: item.HD_IBCOCE,
        vMonth: item.HD_IBMTH,
        vPlanUnPlan: item.HD_IBPRIP,
        vBU: item.HD_BU,
        vBuyer: item.HD_IBBUYE,
        vGroup: item.HD_IBMODL,
        vCAPNo: item.HD_CAPNO,
        vRequestor: item.HD_IBPURC,
        vRemark: item.HD_REM1,
        vApprove1: item.HD_APP1,
        vApprove2: item.HD_APP2,
        vApprove3: item.HD_APP3,
        vApprove4: item.HD_APP4,
        vStatus: item.HD_STATUS,
      });
      // } else {
      // console.log("prheads.vStatus: false");
      // setADRHead({ ...initialStateADRHead });
      // dispatch(prdetailbuyerActions.getEPRDetails("00"));
      // handleCancel();
      // }
    });
  }, [prheadReducer]);

  useEffect(() => {
    const prdetails = prdetailbuyerReducer.result
      ? prdetailbuyerReducer.result
      : [];

    prdetails.map((item) => {
      // console.log("item.PR_IBORTY: " + item.PR_IBORTY);
      // dispatch(paymentActions.getPaymentsBySupplier(item.PR_IBSUNO));
      setItemPRDetail({ ...itemprdetail, vImagePreview2: item.IMAGE });
    });
  }, [prdetailbuyerReducer]);

  useEffect(() => {
    const prconfirmbuyers = prconfirmbuyerReducer.result
      ? prconfirmbuyerReducer.result
      : [];
    prconfirmbuyers.map((item) => {
      console.log("PR_CONFIRM: " + item.PR_CONFIRM);
      setPRConfirmBuyer(item.PR_CONFIRM);
      if (item.PR_CONFIRM === "0") {
        console.log("prconfirm: true");
        let fromStatus = "05";
        let toStatus = "10";
        dispatch(prnumberbuyerActions.getEPRNumbers(fromStatus, toStatus));

        let statusprhead = "15";
        dispatch(
          prheadActions.updateStsEPRHead(adrhead.vPRNumber, statusprhead)
        );
        prheadReducer.result = null;
        prdetailbuyerReducer.result = null;
        setADRHead({
          ...initialStateADRHead,
        });

        handleCancel();
      }
    });
  }, [prconfirmbuyerReducer]);

  useEffect(() => {
    const genpos = genpoReducer.result ? [genpoReducer.result] : [];
    // console.log(JSON.stringify(genpos));
    genpos.map((item) => {
      // console.log(item.message);
      setPONumber({ ...prnumber, vPOSelectNumber: item.message });
      setSuccess(true);
      setLoading(false);
      handleAfterGenPO();
    });
  }, [genpoReducer]);

  const prnumberbuyers = useMemo(() =>
    prnumberbuyerReducer.result ? prnumberbuyerReducer.result : []
  );

  const warehouses = useMemo(() =>
    warehouseReducer.result ? warehouseReducer.result : []
  );

  const bus = useMemo(() => (buReducer.result ? buReducer.result : []));

  const departments = useMemo(() =>
    departmentReducer.result ? departmentReducer.result : []
  );

  const costcenters = useMemo(() =>
    costcenterReducer.result ? costcenterReducer.result : []
  );

  const approves = useMemo(() =>
    approveReducer.result ? approveReducer.result : []
  );

  const genpos = useMemo(() =>
    genpoReducer.result ? genpoReducer.result : []
  );

  const deliverys = useMemo(() =>
    deliveryReducer.result ? deliveryReducer.result : []
  );

  const payments = useMemo(() =>
    paymentReducer.result ? paymentReducer.result : []
  );

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

  const handleSearch = () => {
    if (prnumber.vPRSelectNumber === "") {
      setADRHead({
        ...initialStateADRHead,
      });
      // dispatch(prdetailbuyerActions.getEPRDetailsGenPO("00", "00"));
      setPONumber({ ...prnumber, vPOSelectNumber: "" });
      setGenPODisable(true);
      prdetailbuyerReducer.result = null;
      // deliveryReducer.result = null;
      // paymentReducer.result = null;
    } else {
      let fromStatus = "92";
      let toStatus = "92";

      dispatch(
        prheadActions.getEPRHeads(
          prnumber.vPRSelectNumber,
          fromStatus,
          toStatus
        )
      );
      dispatch(
        prdetailbuyerActions.getEPRDetailsGenPO(
          fromStatus,
          prnumber.vPRSelectNumberLine
        )
      );
      setPONumber({ ...prnumber, vPOSelectNumber: "" });
      setGenPODisable(false);
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

  const handleClose = () => {
    setOpenDialog(false);
    // setItemPRDetail(initialStateItemPRDetail);
    // setSaveDisable(false);
    // setConfirmDisable(false);
    // setAddFreeItem(false);
    // setEditNameDisable(true);
    // setShowRemark(false);
    // setCurrencyDisable(true);
  };

  const handleAfterGenPO = () => {
    let status = "92";
    dispatch(prnumberbuyerActions.getEPRNumbersGenPO(status));
    prheadReducer.result = null;
    prdetailbuyerReducer.result = null;
  };

  const handleCancel = () => {
    setPRNumber({ ...prnumber, vPRSelectNumber: "" });
    setADRHead({ ...initialStateADRHead });
    dispatch(prdetailbuyerActions.getEPRDetails("00"));
    setSearchDisable(false);
    setNewDisable(false);
    setSaveDisable(true);
    setClearDisable(true);

    setEditDisable(true);
    setCreateDisable(true);
    setCancelPRDisable(true);
    setWhsDisable(true);
    setDeptDisable(true);
  };

  const handleButtonClick = () => {
    if (!loading) {
      setSuccess(false);
      setLoading(true);
      timer.current = window.setTimeout(() => {
        setSuccess(true);
        setLoading(false);
      }, 2000);
    }
  };

  const NumberFormatCustom = (props) => {
    const { inputRef, onChange, ...other } = props;

    return (
      <NumberFormat
        {...other}
        getInputRef={inputRef}
        onValueChange={(values) => {
          onChange({
            target: {
              name: props.name,
              value: values.value,
            },
          });
        }}
        thousandSeparator
        isNumericString
        prefix="฿"
      />
    );
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
                    error={true}
                    fullWidth
                    select
                    size="small"
                    variant="outlined"
                    required
                    id="vSelectADRNumber"
                    label="ADR Number"
                    disabled={searchdisable}
                    value={prnumber.vPRSelectNumberLine}
                    onChange={(event) => {
                      // console.log(event.target.value);
                      let getEPRNumberselect = event.target.value
                        .toString()
                        .split("::");
                      let getPRNumberLine = getEPRNumberselect[0]
                        .toString()
                        .split("-");
                      let getPRnumber = getPRNumberLine[0];
                      let getBuyer = getEPRNumberselect[2];

                      // console.log(getPRnumber + "  " + getBuyer);

                      setPRNumber({
                        ...prnumber,
                        vPRSelectNumber: getPRnumber,
                        vPRSelectNumberLine: event.target.value,
                      });
                    }}
                    InputLabelProps={{ shrink: true }}
                    SelectProps={{
                      native: true,
                    }}
                  >
                    <option />
                    {prnumberbuyers.map((option) => (
                      <option key={option.ID} value={option.PRNUMBER}>
                        {option.PRNUMBER}
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
                    onClick={handleSearch}
                  >
                    Search
                  </Button>
                </Grid>
                <Grid item xs={6} md={"auto"}>
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
                </Grid>
                <Grid item xs={6} md={"auto"}>
                  <ColorButtonDeepOrange
                    fullWidth
                    size="medium"
                    id="vSave"
                    variant="contained"
                    color="primary"
                    type="submit"
                    startIcon={<SaveIcon />}
                    disabled={savedisable}
                  >
                    Save
                  </ColorButtonDeepOrange>
                </Grid>
                <Grid item xs={6} md={"auto"}>
                  <ColorButtonDeepBlueGray
                    fullWidth
                    size="medium"
                    id="vCancel"
                    variant="contained"
                    color="secondary"
                    startIcon={<CancelIcon />}
                    disabled={cleardisable}
                    onClick={handleCancel}
                  >
                    Clear
                  </ColorButtonDeepBlueGray>
                </Grid>
              </Grid>

              <Grid container item xs spacing={1}>
                <Grid item xs={6} md={"auto"}>
                  <TextField
                    className={classes.margin}
                    // style={{ maxWidth: 120 }}
                    required
                    disabled={true}
                    size="small"
                    id="vADRNumber"
                    label="ADR Number"
                    placeholder="ADR Number"
                    variant="outlined"
                    value={adrhead.vPRNumber}
                    values={(values.vPRNumber = adrhead.vPRNumber)}
                    onChange={(event) => {
                      // console.log(event.target.value);
                      setADRHead({
                        ...adrhead,
                        vPRNumber: event.target.value,
                      });
                    }}
                    InputLabelProps={{ shrink: true }}
                  />
                </Grid>

                <Grid item xs={6} md={"auto"}>
                  <TextField
                    className={classes.margin}
                    required
                    disabled={true}
                    // disabled={editdisable}
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

                <Grid item xs={6} md={"auto"}>
                  <TextField
                    className={classes.margin}
                    style={{ maxWidth: 100 }}
                    required
                    // disabled={editdisable}
                    disabled={true}
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
                    disabled={editdisable}
                    required
                    error
                    select
                    size="small"
                    variant="outlined"
                    margin="normal"
                    id="vType"
                    label="Type"
                    value={adrhead.vType}
                    values={(values.vType = adrhead.vType)}
                    onChange={(event) => {
                      // console.log(event.target.value);
                      setADRHead({
                        ...adrhead,
                        vType: event.target.value,
                      });
                    }}
                    InputLabelProps={{ shrink: true }}
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
                    disabled={editdisable}
                    required
                    error
                    select
                    size="small"
                    variant="outlined"
                    margin="normal"
                    id="vType"
                    label="BOI"
                    value={adrhead.vType}
                    values={(values.vType = adrhead.vType)}
                    onChange={(event) => {
                      // console.log(event.target.value);
                      setADRHead({
                        ...adrhead,
                        vType: event.target.value,
                      });
                    }}
                    InputLabelProps={{ shrink: true }}
                    SelectProps={{
                      native: true,
                    }}
                  ></TextField>
                </Grid>

                <Grid item xs={6} md={"auto"}>
                  <TextField
                    className={classes.margin}
                    disabled={editdisable}
                    required
                    error
                    select
                    size="small"
                    id="vBU"
                    label="BU"
                    placeholder="Placeholder"
                    variant="outlined"
                    value={adrhead.vBU}
                    values={(values.vBU = adrhead.vBU)}
                    onChange={(event) => {
                      // console.log(event.target.value);
                      setADRHead({
                        ...adrhead,
                        vBU: event.target.value,
                      });

                      // dispatch(
                      //   departmentActions.getDepartments(event.target.value)
                      // );
                    }}
                    InputLabelProps={{ shrink: true }}
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

                <Grid item xs={6} md={"auto"}>
                  <TextField
                    className={classes.margin}
                    disabled={editdisable}
                    required
                    error
                    size="small"
                    id="vCostcenter"
                    label="Costcenter"
                    placeholder="Costcenter"
                    variant="outlined"
                    value={adrhead.vCostcenter}
                    values={(values.vCostcenter = adrhead.vCostcenter)}
                    InputLabelProps={{ shrink: true }}
                  />
                </Grid>

                <Grid item xs={6} md={"auto"}>
                  <TextField
                    className={classes.margin}
                    disabled={editdisable}
                    select
                    required
                    error
                    size="small"
                    id="vCostcenter"
                    label="Vat"
                    placeholder="Costcenter"
                    variant="outlined"
                    value={adrhead.vCostcenter}
                    values={(values.vCostcenter = adrhead.vCostcenter)}
                    InputLabelProps={{ shrink: true }}
                  />
                </Grid>

                <Grid item xs={6} md={1}>
                  <TextField
                    className={classes.margin}
                    disabled={editdisable}
                    select
                    required
                    error
                    size="small"
                    id="vAccountant"
                    label="Accountant"
                    placeholder="Accountant"
                    variant="outlined"
                    value={adrhead.vRequestor}
                    values={(values.vRequestor = adrhead.vRequestor)}
                    InputLabelProps={{ shrink: true }}
                  />
                </Grid>

                <Grid item xs={6} md={"auto"}>
                  <TextField
                    className={classes.margin}
                    disabled={true}
                    required
                    size="small"
                    id="vRequestor"
                    label="Requestor"
                    placeholder="Requestor"
                    variant="outlined"
                    value={adrhead.vRequestor}
                    values={(values.vRequestor = adrhead.vRequestor)}
                    InputLabelProps={{ shrink: true }}
                  />
                </Grid>
              </Grid>

              <Grid container item xs={12} spacing={1}>
                <Grid item xs={6} md={3}>
                  <TextField
                    className={classes.margin}
                    disabled={editdisable}
                    fullWidth
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
                    }}
                    InputLabelProps={{ shrink: true }}
                  />
                </Grid>
              </Grid>

              <Grid container item xs spacing={1}>
                <Grid item xs={6} md={"auto"}>
                  <TextField
                    className={classes.margin}
                    style={{ width: "200px" }}
                    error={true}
                    disabled={editdisable}
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
                <Grid item xs={6} md={"auto"}>
                  <TextField
                    className={classes.margin}
                    style={{ width: "200px" }}
                    error={true}
                    disabled={editdisable}
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
                <Grid item xs={6} md={"auto"}>
                  <TextField
                    className={classes.margin}
                    style={{ width: "200px" }}
                    error={true}
                    disabled={editdisable}
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
                <Grid item xs={6} md={"auto"}>
                  <TextField
                    className={classes.margin}
                    style={{ width: "200px" }}
                    error={true}
                    disabled={editdisable}
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
                <Grid item xs={6} md={"auto"}>
                  <Button
                    className={classes.margin}
                    fullWidth
                    size="medium"
                    id="vCancelPR"
                    variant="contained"
                    color="secondary"
                    // style={{ backgroundColor: green[500] }}
                    startIcon={<DeleteIcon />}
                    disabled={cancelprdisable}
                    // onClick={handleCancelPR}
                  >
                    Cancel ADR
                  </Button>
                  {/* {loadingcancelpr && (
                    <CircularProgress
                      size={24}
                      className={classes.buttonProgress}
                    />
                  )} */}
                </Grid>
                <Grid item xs={6} md={"auto"}>
                  <ColorButton
                    className={classes.margin}
                    fullWidth
                    size="medium"
                    id="vSubmitPH"
                    variant="contained"
                    color="secondary"
                    startIcon={<SendIcon />}
                    disabled={cancelprdisable}
                    // onClick={handleSubmitPH}
                  >
                    Submit
                  </ColorButton>
                  {/* {loadingsubmitph && (
                    <CircularProgress
                      size={24}
                      className={classes.buttonProgress}
                    />
                  )} */}
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

    const items = itemReducer.result ? itemReducer.result : [];
    const itemunits = itemunitReducer.result ? itemunitReducer.result : [];
    const phgroups = phgroupReducer.result ? phgroupReducer.result : [];
    const phbuyers = phbuyerReducer.result ? phbuyerReducer.result : [];
    const suppliers = supplierReducer.result ? supplierReducer.result : [];

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
            ADR Number : {adrhead.vPRNumber}
            {itemprdetail.vItemLine
              ? ` - Line : ${itemprdetail.vItemLine}`
              : ""}
          </DialogTitle>
          <DialogContent>
            <Grid container item xs={12} spacing={2}>
              <Grid item xs={5}>
                <Autocomplete
                  error
                  className={classes.margin}
                  autoFocus
                  required
                  fullWidth
                  disabled={addfreeitem}
                  size="small"
                  id="vItemNoAuto"
                  options={items}
                  getOptionLabel={(option) => option.ITEM}
                  value={itemprdetail.vItemDesc2}
                  values={(values.vItemNo = itemprdetail.vItemNo)}
                  onChange={(event, values) => {
                    // console.log(values);
                    if (values) {
                      // console.log(
                      //   "Price: " +
                      //     values.MMPUPR +
                      //     " : Order Type: " +
                      //     values.MBORTY +
                      //     " Currency: " +
                      //     values.MMCUCD
                      // );

                      setItemPRDetail({
                        ...itemprdetail,
                        vQty: "",
                        vTotal: "",
                        // vItemNo: { MMITNO: values.MMITNO },
                        vItemNo: values.MMITNO,
                        vItemDesc1: values.MMFUDS.replace(
                          /[^\w\s\ก-๙\-’/`~!#*$@_%+=.,^&(){}[\]|:”"<>?\\]/g,
                          ""
                        ),
                        vItemDesc2: { ITEM: values.ITEM },
                        vUnit: values.MMUNMS,
                        vSupplierNo: values.MMSUNO,
                        vSupplierName: values.SASUNM,
                        vSupplierDesc: { SUPPLIER: values.SUPPLIER },
                        // vPrice: values.MMPUPR,
                        vPrice: addfreeitem ? values.MMPUPR : "0",
                        vVat: values.MMVTCP,
                        vCurrency: values.MMCUCD,
                        vOrdertype: values.MBORTY,
                      });
                      dispatch(itemunitActions.getItemUnits(values.MMITNO));

                      if (
                        values.MMITNO.substr(0, 2) === "OH" ||
                        values.MMITNO.substr(0, 2) === "BU" ||
                        values.MMITNO.substr(0, 2) === "EL"
                      ) {
                        setEditNameDisable(false);
                      } else {
                        setEditNameDisable(true);
                      }
                    }
                  }}
                  renderInput={(params) => (
                    <TextField
                      {...params}
                      error={true}
                      id="vItemNo"
                      label="Item No"
                      required
                      InputLabelProps={{ shrink: true }}
                    />
                  )}
                />
              </Grid>
              <Grid item xs>
                <TextField
                  className={classes.margin}
                  error={true}
                  fullWidth
                  disabled={editnamedisable}
                  margin="dense"
                  id="vItemName"
                  label="Item Name"
                  type="text"
                  value={itemprdetail.vItemDesc1}
                  values={(values.vItemDesc1 = itemprdetail.vItemDesc1)}
                  InputLabelProps={{ shrink: true }}
                  onChange={(event) => {
                    console.log(event.target.value);

                    if (event.target.value === "'") {
                      console.log("true");
                    } else {
                      console.log("false");
                    }

                    setItemPRDetail({
                      ...itemprdetail,
                      vItemDesc1: event.target.value.replace(
                        /[^\w\s\ก-๙\-’/`~!#*$@_%+=.,^&(){}[\]|:”"<>?\\]/g,
                        ""
                      ),
                    });
                  }}
                />
              </Grid>
            </Grid>

            <Grid container item xs={12} spacing={2}>
              <Grid item xs={5}>
                <TextField
                  required
                  error={true}
                  fullWidth
                  disabled={editdisable}
                  margin="dense"
                  id="vQty"
                  label="Qty"
                  type="number"
                  value={itemprdetail.vQty}
                  values={(values.vQty = itemprdetail.vQty)}
                  InputLabelProps={{ shrink: true }}
                  onChange={(event) => {
                    // console.log(event.target.value);
                    let qty = event.target.value;
                    let price = itemprdetail.vPrice;
                    setItemPRDetail({
                      ...itemprdetail,
                      vQty: event.target.value,
                      vTotal: (qty * price).toFixed(4),
                    });
                  }}
                />
              </Grid>
              <Grid item xs>
                <TextField
                  className={classes.margin}
                  fullWidth
                  disabled={editdisable}
                  required
                  error={true}
                  select
                  margin="dense"
                  variant="standard"
                  size="small"
                  id="vUnit"
                  label="Unit"
                  value={itemprdetail.vUnit}
                  values={(values.vUnit = itemprdetail.vUnit)}
                  InputLabelProps={{ shrink: true }}
                  onChange={(event) => {
                    // console.log(event.target.value);
                    setItemPRDetail({
                      ...itemprdetail,
                      vUnit: event.target.value,
                    });
                  }}
                  SelectProps={{
                    native: true,
                  }}
                >
                  <option />
                  {itemunits.map((option) => (
                    <option key={option.ID} value={option.MMUNMS}>
                      {option.MMUNMS}
                    </option>
                  ))}
                </TextField>
              </Grid>
            </Grid>

            <TextField
              // required
              fullWidth
              // disabled="true"
              margin="dense"
              id="vPHRemarkDetail"
              label="PH Remark"
              type="text"
              value={itemprdetail.vPHRemarkDetail}
              values={(values.vPHRemarkDetail = itemprdetail.vPHRemarkDetail)}
              onChange={(event) => {
                // console.log(event.target.value);
                setItemPRDetail({
                  ...itemprdetail,
                  vPHRemarkDetail: event.target.value,
                });
              }}
            />
            <TextField
              fullWidth
              // disabled="true"
              // margin="dense"
              id="vImage"
              label="Image"
              type="file"
              // value={itemprdetail.vPHRemarkDetail}
              // values={(values.vPHRemarkDetail = itemprdetail.vPHRemarkDetail)}
              InputLabelProps={{ shrink: true, required: true }}
              onChange={(event) => {
                console.log(event.target.files);

                const reader = new FileReader();
                reader.readAsDataURL(event.target.files[0]);

                reader.onload = () => {
                  console.log("called: ", reader.result);
                  // setBase64IMG(reader.result);
                };

                if (event.target.files[0]) {
                  setItemPRDetail({
                    ...itemprdetail,
                    vImageFile: event.target.files[0],
                    vImagePreview: URL.createObjectURL(event.target.files[0]),
                    vImagePreview2: reader.result,
                  });
                }
              }}
            />

            <Grid item xs>
              <img
                src={itemprdetail.vImagePreview}
                // src={itemprdetail.vImagePreview2}
                // src={`data:image/png;base64,${itemprdetail.vImagePreview2}`}
                width="100%"
                height="100%"
              />

              {/* image={`data:image/jpeg;base64,${adrhead.vApproveSign2}`} */}
            </Grid>
          </DialogContent>

          <DialogActions>
            <div>
              <Button onClick={handleClose} color="default">
                Close
              </Button>
              <Button
                disabled={savedisable}
                type="submit"
                color="primary"
                onClick={(event) => {
                  // values.vNameRemarkDetail = itemprdetail.vNameRemarkDetail;
                  // values.vDescRemarkDetail = itemprdetail.vDescRemarkDetail;
                  // values.vTextRemarkDetail = itemprdetail.vTextRemarkDetail;

                  if (itemprdetail.vItemLine === "") {
                    setCreate(true);
                  } else {
                    setUpdate(true);
                  }
                }}
              >
                Save
              </Button>
              {loading && (
                <CircularProgress
                  size={24}
                  className={classes.buttonProgress}
                />
              )}
              <Button
                disabled={confirmdisable}
                type="submit"
                color="secondary"
                onClick={(event) => {
                  setConfirm(true);
                }}
                style={{ display: "" }}
              >
                Confirm
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
      title: "Line",
      field: "PR_IBPLPS",
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
          {item.PR_IBPLPS}
        </Typography>
      ),
    },
    {
      title: "Group",
      field: "PR_SPORDER",
      type: "numeric",
      editable: "never",
      width: 80,
      headerStyle: { maxWidth: 80, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.PR_SPORDER}
        </Typography>
      ),
    },
    {
      title: "Item No",
      field: "PR_IBITNO",
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
          {item.PR_IBITNO}
        </Typography>
      ),
    },
    {
      title: "Item Name",
      field: "PR_IBPITT",
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
          {item.PR_IBPITT}
        </Typography>
      ),
    },
    {
      title: "Unit",
      field: "PR_IBPUUN",
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
          {item.PR_IBPUUN}
        </Typography>
      ),
    },
    {
      title: "Qty",
      field: "PR_IBORQA",
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
            value={item.PR_IBORQA}
            displayType={"text"}
            thousandSeparator={true}
            // prefix={"$"}
          />
        </Typography>
      ),
    },
    {
      title: "U/P",
      field: "PR_IBPUPR",
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
            value={item.PR_IBPUPR}
            displayType={"text"}
            thousandSeparator={true}
            // prefix={"$"}
          />
        </Typography>
      ),
    },
    {
      title: "Vat.",
      field: "PR_IBVTCD",
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
          <NumberFormat
            value={item.PR_IBVTCD}
            displayType={"text"}
            thousandSeparator={true}
            // prefix={"$"}
          />
        </Typography>
      ),
    },
    // {
    //   title: "Amt.",
    //   field: "PR_IBTOTA",
    //   editable: "never",
    //   headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
    //   cellStyle: {
    //     textAlign: "right",
    //     borderLeft: 1,
    //     borderRight: 1,
    //     borderBottom: 1,
    //     borderTop: 1,
    //     borderColor: "#E0E0E0",
    //     borderStyle: "solid",
    //     paddingLeft: "6px",
    //     paddingRight: "6px",
    //     paddingBottom: "12px",
    //     paddingTop: "12px",
    //   },
    //   render: (item) => (
    //     <Typography variant="body1" noWrap>
    //       <NumberFormat
    //         value={item.PR_IBTOTA}
    //         displayType={"text"}
    //         thousandSeparator={true}
    //         // prefix={"$"}
    //       />
    //     </Typography>
    //   ),
    // },
    // {
    //   title: "Curr.",
    //   field: "PR_IBCUCD",
    //   editable: "never",
    //   headerStyle: { maxWidth: 50, whiteSpace: "nowrap", textAlign: "center" },
    //   cellStyle: {
    //     textAlign: "center",
    //     borderLeft: 1,
    //     borderRight: 1,
    //     borderBottom: 1,
    //     borderTop: 1,
    //     borderColor: "#E0E0E0",
    //     borderStyle: "solid",
    //     paddingLeft: "6px",
    //     paddingRight: "6px",
    //     paddingBottom: "12px",
    //     paddingTop: "12px",
    //   },
    //   render: (item) => (
    //     <Typography variant="body1" noWrap>
    //       {item.PR_IBCUCD}
    //     </Typography>
    //   ),
    // },
    {
      title: "Deli. Date",
      field: "PR_IBDWDT",
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
          {moment(item.PR_IBDWDT).format("DD/MM/YYYY")}
        </Typography>
      ),
    },
    {
      title: "Supp. No",
      field: "PR_IBSUNO",
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
          {item.PR_IBSUNO}
        </Typography>
      ),
    },
    // {
    //   title: "Supp. Name",
    //   field: "SASUNM",
    //   editable: "never",
    //   headerStyle: { maxWidth: 150, whiteSpace: "nowrap", textAlign: "center" },
    //   cellStyle: {
    //     textAlign: "left",
    //     borderLeft: 1,
    //     borderRight: 1,
    //     borderBottom: 1,
    //     borderTop: 1,
    //     borderColor: "#E0E0E0",
    //     borderStyle: "solid",
    //     paddingLeft: "6px",
    //     paddingRight: "6px",
    //     paddingBottom: "12px",
    //     paddingTop: "12px",
    //   },
    //   render: (item) => (
    //     <Typography variant="body1" noWrap>
    //       {item.SASUNM}
    //     </Typography>
    //   ),
    // },
    {
      title: "Order Typ.",
      field: "PR_IBORTY",
      editable: "never",
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
          {item.PR_IBORTY}
        </Typography>
      ),
    },
    {
      title: "HD_IBPURC",
      field: "HD_IBPURC",
      editable: "never",
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
          {item.HD_IBPURC}
        </Typography>
      ),
    },
    {
      title: "PR_IBODI",
      field: "PR_IBODI1",
      editable: "never",
      headerStyle: { maxWidth: 60, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.PR_IBODI1}
        </Typography>
      ),
    },
    // {
    //   title: "V Amt.",
    //   field: "PR_VTCHARGE",
    //   editable: "never",
    //   headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
    //   cellStyle: {
    //     textAlign: "right",
    //     borderLeft: 1,
    //     borderRight: 1,
    //     borderBottom: 1,
    //     borderTop: 1,
    //     borderColor: "#E0E0E0",
    //     borderStyle: "solid",
    //     paddingLeft: "6px",
    //     paddingRight: "6px",
    //     paddingBottom: "12px",
    //     paddingTop: "12px",
    //   },
    //   render: (item) => (
    //     <Typography variant="body1" noWrap>
    //       <NumberFormat
    //         value={item.PR_VTCHARGE}
    //         displayType={"text"}
    //         thousandSeparator={true}
    //         // prefix={"$"}
    //       />
    //     </Typography>
    //   ),
    // },
    {
      title: "Remark",
      field: "PR_REM3",
      editable: "never",
      headerStyle: { maxWidth: 150, whiteSpace: "nowrap", textAlign: "left" },
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
          {item.PR_REM3}
        </Typography>
      ),
    },
    {
      title: "PH Remark",
      field: "PHREMARK",
      editable: "never",
      headerStyle: { maxWidth: 150, whiteSpace: "nowrap", textAlign: "left" },
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
          {item.PHREMARK}
        </Typography>
      ),
    },
    {
      title: "PH Remark2",
      field: "PR_PHREMARK2",
      editable: "never",
      headerStyle: { maxWidth: 150, whiteSpace: "nowrap", textAlign: "left" },
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
          {item.PR_PHREMARK2}
        </Typography>
      ),
    },
    {
      title: "PH Remark3",
      field: "PR_PHREMARK3",
      editable: "never",
      headerStyle: { maxWidth: 150, whiteSpace: "nowrap", textAlign: "left" },
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
          {item.PR_PHREMARK3}
        </Typography>
      ),
    },
    {
      title: "Quotation",
      field: "PR_PHREMARK4",
      editable: "never",
      headerStyle: { maxWidth: 150, whiteSpace: "nowrap", textAlign: "left" },
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
          {item.PR_PHREMARK4}
        </Typography>
      ),
    },
  ];

  return (
    <div className={classes.root}>
      {/* Grid */}
      <Formik
        initialValues={{
          vPRNumber: "",
          vDate: "",
          vWarehouse: "",
          vCostcenter: "",
          vMonth: "",
          vPlanUnPlan: "",
          vBU: "",
          vCAPNo: "",
          vRequestor: "",
          vRemark: "",
          vApprove1: "",
          vApprove2: "",
          vDelivery: "",
          vPayment: "",
        }}
        onSubmit={(values, { setSubmitting }) => {
          // alert(JSON.stringify(values));
          let formData = new FormData();
          formData.append("vPRNumber", values.vPRNumber);
          formData.append("vDate", values.vDate);
          formData.append("vWarehouse", values.vWarehouse);
          formData.append("vCostcenter", values.vCostcenter);
          formData.append("vMonth", values.vMonth);
          formData.append("vPlanUnPlan", "5");
          formData.append("vBU", values.vBU);
          formData.append("vCAPNo", values.vCAPNo);
          formData.append(
            "vRequestor",
            adrhead.vRequestor
              ? adrhead.vRequestor
              : loginActions.getTokenUsername()
          );
          formData.append("vRemark", values.vRemark);
          formData.append("vApprove1", values.vApprove1);
          formData.append("vApprove2", values.vApprove2);
          formData.append("vStatus", adrhead.vStatus ? adrhead.vStatus : "00");

          let status = "92";
          dispatch(
            genpoActions.genPONumber(
              status,
              prnumber.vPRSelectNumberLine,
              adrhead.vDelivery ? adrhead.vDelivery : "null",
              adrhead.vPayment
            )
          );
          setGenPODisable(true);
          setSuccess(false);
          setLoading(true);
        }}
      >
        {(props) => showForm(props)}
      </Formik>

      {/* Plan PR Table */}
      <p>#Debug {JSON.stringify(file)}</p>
      <MaterialTable
        id="root_pr"
        title={`ADR Number : ${adrhead.vStatus}`}
        columns={columns}
        data={prdetailbuyerReducer.result ? prdetailbuyerReducer.result : []}
        // isLoading={prdetailbuyerReducer.result ? false : true}
        components={{
          Toolbar: (props) => (
            <div>
              <MTableToolbar {...props} />
              <div style={{ padding: "0px 10px" }}>
                <Button
                  fullWidth
                  // disabled={createdisable}
                  variant="contained"
                  color="primary"
                  // component={Link}
                  // to="/stock/create"
                  // startIcon={<AddCircleIcon />}
                  onClick={(event, rowData) => {
                    // let phgroup = "PH";
                    setItemPRDetail({ ...itemprdetail, vAddFreeItem: "1" });
                    setSelectedProduct("rowData");
                    setOpenDialog(true);
                    setEditDisable(false);
                    setEditNameDisable(true);
                    setConfirmDisable(true);

                    // dispatch(prdetailbuyerActions.getADRDetail());
                    // dispatch(prdetailbuyerActions.getImage("A001_1.png"));

                    // dispatch(itemActions.getItems(adrhead.vWarehouse));
                    // dispatch(phgroupActions.getPHGroups(phgroup));
                  }}
                >
                  Add Item
                </Button>
              </div>
            </div>
          ),
        }}
        options={{
          exportButton: true,
          // toolbar: false,
          paging: false,
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
            // backgroundColor: "red",
            // padding: "5px",
            // whiteSpace: "normal",
            // wordWrap: "break-word",
            // wordBreak: "break-all"
          },
          cellStyle: {
            textAlign: "center",
            borderLeft: 1,
            borderRight: 1,
            borderBottom: 1,
            borderTop: 1,
            borderColor: "#E0E0E0",
            borderStyle: "solid",
          },
          // rowStyle: rowData => ({
          //   // backgroundColor:
          //   //   selectedRow && selectedRow.tableData.id === rowData.tableData.id
          //   //     ? "#EEE"
          //   //     : "#FFF"
          // }),
          fixedColumns: {
            // left: 2
          },
        }}
        editable={{
          onRowUpdate: (newData, oldData) => {
            dispatch(
              prdetailbuyerActions.updateEPRDetailGrouping(
                oldData.PR_IBPLPN,
                oldData.PR_IBPLPS,
                oldData.PR_SPORDER,
                moment(newData.PR_IBDWDT).format("YYYY-MM-DD")
              )
            );
            return new Promise((resolve, reject) => {
              setTimeout(resolve, 500);
            });
          },
        }}
      />

      {/* Dialog */}
      <Formik
        initialValues={{
          vPRNumber: adrhead.vPRNumber,
          vPlanUnPlan: adrhead.vPlanUnPlan,
          vItemLine: "",
          vItemNo: "", //{ MMITNO: "" },
          vItemDesc1: "",
          vItemDesc2: "",
          vQty: "",
          vUnit: "",
          vDateDetail: moment(new Date()).format("YYYY-MM-DD"), //"2018-12-01"
          vSupplierNo: "",
          vSupplierName: "",
          vPrice: "",
          vVat: "",
          vCurrency: "",
          vOrdertype: "",
          vTotal: "",
          vCostcenterDetail: "",
          vPHGroupDetail: "",
          vBuyerDetail: "",
          vRemarkDetail: "",
          vPHRemarkDetail: "",
          vNameRemarkDetail: "",
          vDescRemarkDetail: "",
          vTextRemarkDetail: "",
        }}
        onSubmit={(values, { setSubmitting }) => {
          // alert(JSON.stringify(values));
          let formData = new FormData();
          // formData.append("uploadFile", itemprdetail.vImage);
          formData.append("uploadFile", itemprdetail.vImageFile);
          formData.append("fileName", "A001_1.jpg");
          dispatch(prdetailbuyerActions.addImage(formData, props.history));
        }}
      >
        {(props) => showDialog(props)}
      </Formik>
    </div>
  );
};
