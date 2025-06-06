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
import ArrowDropDownIcon from "@material-ui/icons/ArrowDropDown";
import ArrowDropUpIcon from "@material-ui/icons/ArrowDropUp";
import BookmarkIcon from "@material-ui/icons/Bookmark";
import CircularProgress from "@material-ui/core/CircularProgress";
import { Formik, Form, Field } from "formik";
import { red, green, purple, deepOrange } from "@material-ui/core/colors/";
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

const theme = createMuiTheme({
  palette: {
    // primary: {
    //   500: "#0FF",
    // },
    fourth: {
      500: "#0FF",
    },
  },
});

const accent = purple["A200"]; // #E040FB
// const accent = purple.A200; // #E040FB (alternative method)

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
  const initialStatePRHead = {
    vPRNumber: "",
    vDate: moment(new Date()).format("YYYY-MM-DD"),
    vWarehouse: "",
    vCostcenter: "",
    vMonth: "",
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
  const [prhead, setPRHead] = useState(initialStatePRHead);
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
    vPHRemarkDetail: "",
    vNameRemarkDetail: "",
    vDescemarkDetail: "",
    vTextRemarkDetail: "",
    vAddFreeItem: "",
  };
  const [itemprdetail, setItemPRDetail] = useState(initialStateItemPRDetail);
  const [searchdisable, setSearchDisable] = useState(false);
  const [newdisable, setNewDisable] = useState(false);
  const [editdisable, setEditDisable] = useState(true);
  const [genpodisable, setGenPODisable] = useState(true);
  const [createdisable, setCreateDisable] = useState(true);
  const [cancelprdisable, setCancelPRDisable] = useState(true);
  const [savedisable, setSaveDisable] = useState(false);
  const [confirmdisable, setConfirmDisable] = useState(false);
  const [addfreeitem, setAddFreeItem] = useState(false);
  const [editnamedisable, setEditNameDisable] = useState(true);
  const [showremark, setShowRemark] = useState(false);
  const [deliverydisable, setDeliveryDisable] = useState(true);
  const [reservedisable, setReserveDisable] = useState(true);
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
  const [loadingreserve, setLoadingReserve] = useState(false);
  const [success, setSuccess] = useState(false);
  const timer = useRef();

  useEffect(() => {
    // console.log("dispatch prnumberbuyerActions");
    let status = "92";
    dispatch(prnumberbuyerActions.getEPRNumbersGenPO(status));
    dispatch(warehouseActions.getWarehouse());
    dispatch(buActions.getBU());
    dispatch(approveActions.getApproves());
    dispatch(buyerActions.getBuyers());
    dispatch(supplierActions.getSuppliers());
    dispatch(deliveryActions.getDeliverys());
    dispatch(paymentActions.getPayments());
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
      setPRHead({
        ...prhead,
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
      // setPRHead({ ...initialStatePRHead });
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
      setItemPRDetail({ ...itemprdetail, vSupplierNo: item.PR_IBSUNO });
      setPRHead({
        ...prhead,
        vPayment: item.IITEPY,
      });

      if (
        item.PR_IBORTY == "705" ||
        item.PR_IBORTY == "707" ||
        item.PR_IBORTY == "708" ||
        item.PR_IBORTY == "713"
      ) {
        // console.log("true");
        setDeliveryDisable(false);
      } else {
        setDeliveryDisable(true);
      }
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
          prheadActions.updateStsEPRHead(prhead.vPRNumber, statusprhead)
        );
        prheadReducer.result = null;
        prdetailbuyerReducer.result = null;
        setPRHead({
          ...initialStatePRHead,
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

  const BU = useMemo(() => (buReducer.result ? buReducer.result : []));

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

  const buttonClassname = clsx({
    [classes.buttonSuccess]: success,
  });

  const ValidationTextField = withStyles({
    root: {
      "& input:valid + fieldset": {
        borderColor: "green",
        borderWidth: 2,
      },
      "& input:invalid + fieldset": {
        borderColor: "red",
        borderWidth: 2,
      },
      "& input:valid:focus + fieldset": {
        borderLeftWidth: 6,
        padding: "4px !important", // override inline-style
      },
    },
  })(TextField);

  const ColorButton = withStyles((theme) => ({
    root: {
      color: theme.palette.getContrastText(green[500]),
      backgroundColor: green[500],
      "&:hover": {
        backgroundColor: green[700],
      },
    },
  }))(Button);

  const ColorButtonOrange = withStyles((theme) => ({
    root: {
      color: theme.palette.getContrastText(deepOrange[500]),
      backgroundColor: deepOrange[500],
      "&:hover": {
        backgroundColor: deepOrange[700],
      },
    },
  }))(Button);

  const theme = createMuiTheme({
    palette: {
      primary: green,
    },
  });

  const handleSearch = () => {
    if (prnumber.vPRSelectNumber === "") {
      setPRHead({
        ...initialStatePRHead,
      });
      dispatch(prdetailbuyerActions.getEPRDetailsGenPO("00", "00"));
      setPONumber({ ...prnumber, vPOSelectNumber: "" });
      setGenPODisable(true);
      deliveryReducer.result = null;
      paymentReducer.result = null;
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

  const handleGenPO = () => {
    let status = "92";
    // dispatch(
    //   genpoActions.genPONumber(
    //     status,
    //     prnumber.vPRSelectNumberLine,
    //     prhead.vDelivery,
    //     prhead.vPayment
    //   )
    // );
    setGenPODisable(true);
    setSuccess(false);
    setLoading(true);
  };

  const handleAfterGenPO = () => {
    let status = "92";
    dispatch(prnumberbuyerActions.getEPRNumbersGenPO(status));
    prheadReducer.result = null;
    prdetailbuyerReducer.result = null;
  };

  const handleCancel = () => {
    setPRNumber({ ...prnumber, vPRSelectNumber: "" });
    setPRHead({ ...initialStatePRHead });
    dispatch(prdetailbuyerActions.getEPRDetails("00"));
    setSearchDisable(false);
    setNewDisable(false);
    setEditDisable(true);
    setCreateDisable(true);
    setCancelPRDisable(true);
    setWhsDisable(true);
    setDeptDisable(true);
  };

  const handleClose = () => {
    setOpenDialog(false);
    setItemPRDetail(initialStateItemPRDetail);
    setSaveDisable(false);
    setConfirmDisable(false);
    setAddFreeItem(false);
    setEditNameDisable(true);
    setShowRemark(false);
  };

  const handleReserve = () => {
    let status = "12"; //Reserve
    setReserveDisable(true);
    setSuccess(false);
    setLoadingReserve(true);

    dispatch(prdetailbuyerActions.updateEPRReserve(prhead.vPRNumber, status));
    setTimeout(() => {
      setReserveDisable(true);
      setPRNumber({ ...prnumber, vPRSelectNumber: "" });
      setPRHead({ ...initialStatePRHead });
      prheadReducer.result = null;
      prdetailbuyerReducer.result = null;
      let status = "92";
      dispatch(prnumberbuyerActions.getEPRNumbersGenPO(status));

      setGenPODisable(true);
      setSuccess(true);
      setLoadingReserve(false);
    }, 500);
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
                    error={true}
                    fullWidth
                    select
                    size="small"
                    variant="outlined"
                    required
                    id="vSelectPRNumber"
                    label="EPR Number"
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
                <Grid
                  item
                  xs={12}
                  sm={1}
                  className={(classes.margin, classes.wrapper)}
                >
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
                <Grid className={(classes.margin, classes.wrapper)}>
                  <a
                    href={`${
                      process.env.REACT_APP_API_URL
                    }/br_api/api_report/viewepr/${loginActions.getTokenCono()}/${loginActions.getTokenDivi()}/${
                      prnumber.vPRSelectNumber
                    }`}
                    target="_blank"
                    style={{ textDecoration: "none" }}
                  >
                    <Button
                      fullWidth
                      variant="contained"
                      color="secondary"
                      disabled={searchdisable}
                      startIcon={<SearchIcon />}
                    >
                      View EPR
                    </Button>
                  </a>
                </Grid>
                <Grid className={(classes.margin, classes.wrapper)}>
                  <ColorButton
                    fullWidth
                    size="medium"
                    id="vSearch"
                    variant="contained"
                    color="primary"
                    disabled={genpodisable}
                    startIcon={<SearchIcon />}
                    type="submit"
                    // onClick={handleGenPO}
                  >
                    Gen PO
                  </ColorButton>
                  {loading && (
                    <CircularProgress
                      size={24}
                      className={classes.buttonProgress}
                    />
                  )}
                </Grid>

                {/* <Grid className={(classes.margin, classes.wrapper)}>
                  <ColorButtonOrange
                    // fullWidth
                    size="medium"
                    id="vReserve"
                    variant="contained"
                    color="primary"
                    startIcon={<BookmarkIcon />}
                    disabled={genpodisable}
                    onClick={handleReserve}
                  >
                    Reserve
                  </ColorButtonOrange>
                  {loadingreserve && (
                    <CircularProgress
                      size={24}
                      className={classes.buttonProgress}
                    />
                  )}
                </Grid> */}

                <TextField
                  className={classes.margin}
                  style={{ maxWidth: 200 }}
                  disabled={true}
                  size="small"
                  id="vPONumber"
                  label="PO Number"
                  placeholder="PO Number"
                  variant="outlined"
                  value={ponumber.vPOSelectNumber}
                  onChange={(event) => {
                    // handleSearch();
                    // console.log(event.target.value);
                    // setPRHead({
                    //   ...prhead,
                    //   vPRNumber: event.target.value,
                    // });
                  }}
                  InputLabelProps={{ shrink: true }}
                />
              </Grid>

              <Grid container item xs className={classes.margin}>
                <TextField
                  className={classes.margin}
                  style={{ maxWidth: 120 }}
                  required
                  disabled={true}
                  size="small"
                  id="vPRNumber"
                  label="EPR Number"
                  placeholder="EPR Number"
                  variant="outlined"
                  value={prhead.vPRNumber}
                  values={(values.vPRNumber = prhead.vPRNumber)}
                  onChange={(event) => {
                    // console.log(event.target.value);
                    setPRHead({
                      ...prhead,
                      vPRNumber: event.target.value,
                    });
                  }}
                  InputLabelProps={{ shrink: true }}
                />
                <TextField
                  className={classes.margin}
                  style={{ maxWidth: 180 }}
                  required
                  disabled={true}
                  // disabled={editdisable}
                  type="date"
                  size="small"
                  id="vDate"
                  label="Date"
                  variant="outlined"
                  defaultValue={prhead.vDate}
                  value={prhead.vDate}
                  values={(values.vDate = prhead.vDate)}
                  onChange={(event) => {
                    // console.log("onChange: " + event.target.value.substr(2, 2) +
                    // event.target.value.substr(5, 2));
                    var dateNow = new Date();
                    if (
                      event.target.value < moment(dateNow).format("YYYY-MM-DD")
                    ) {
                      alert("Date not less than present day.");
                    } else {
                      setPRHead({
                        ...prhead,
                        vDate: event.target.value,
                        vMonth:
                          event.target.value.substr(2, 2) +
                          event.target.value.substr(5, 2),
                      });
                    }
                  }}
                  InputLabelProps={{ shrink: true, required: true }}
                />

                <TextField
                  className={classes.margin}
                  style={{ width: "150px" }}
                  disabled={true}
                  // disabled={whsdisable}
                  select
                  size="small"
                  variant="outlined"
                  margin="normal"
                  required
                  id="vWarehouse"
                  label="Warehouse"
                  value={prhead.vWarehouse}
                  values={(values.vWarehouse = prhead.vWarehouse)}
                  onChange={(event) => {
                    // console.log(event.target.value);
                    setPRHead({
                      ...prhead,
                      vWarehouse: event.target.value,
                    });
                  }}
                  InputLabelProps={{ shrink: true }}
                  SelectProps={{
                    native: true,
                  }}
                >
                  <option />
                  {warehouses.map((option) => (
                    <option key={option.ID} value={option.MWWHLO}>
                      {option.WAREHOUSE}
                    </option>
                  ))}
                </TextField>

                <TextField
                  className={classes.margin}
                  style={{ maxWidth: 100 }}
                  required
                  disabled={deptdisable}
                  select
                  size="small"
                  id="vBU"
                  label="BU"
                  placeholder="Placeholder"
                  variant="outlined"
                  value={prhead.vBU}
                  values={(values.vBU = prhead.vBU)}
                  onChange={(event) => {
                    // console.log(event.target.value);
                    setPRHead({
                      ...prhead,
                      vBU: event.target.value,
                    });

                    dispatch(
                      departmentActions.getDepartments(event.target.value)
                    );
                  }}
                  InputLabelProps={{ shrink: true }}
                  SelectProps={{
                    native: true,
                  }}
                >
                  <option />
                  {BU.map((option) => (
                    <option key={option.ID} value={option.S1STID}>
                      {option.BU}
                    </option>
                  ))}
                </TextField>

                <TextField
                  className={classes.margin}
                  style={{ maxWidth: 120 }}
                  required
                  // disabled={editdisable}
                  disabled={true}
                  size="small"
                  id="vCostcenter"
                  label="Costcenter"
                  placeholder="Costcenter"
                  variant="outlined"
                  value={prhead.vCostcenter}
                  values={(values.vCostcenter = prhead.vCostcenter)}
                  InputLabelProps={{ shrink: true }}
                />

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
                  value={prhead.vMonth}
                  values={(values.vMonth = prhead.vMonth)}
                  InputLabelProps={{ shrink: true }}
                />

                <TextField
                  className={classes.margin}
                  style={{ maxWidth: 120 }}
                  required
                  // disabled={editdisable}
                  disabled={true}
                  size="small"
                  id="vPlanUnPlan"
                  label="Plan / UnPlan"
                  placeholder="Plan / UnPlan"
                  variant="outlined"
                  value={prhead.vPlanUnPlan}
                  values={(values.vPlanUnPlan = prhead.vPlanUnPlan)}
                  InputLabelProps={{ shrink: true }}
                />

                <TextField
                  className={classes.margin}
                  style={{ maxWidth: 100 }}
                  // required
                  // disabled={editdisable}
                  disabled={true}
                  size="small"
                  id="vCAPNo"
                  label="CAP No"
                  placeholder="CAP No"
                  variant="outlined"
                  value={prhead.vCAPNo}
                  values={(values.vCAPNo = prhead.vCAPNo)}
                  onChange={(event) => {
                    // console.log(event.target.value);
                    setPRHead({
                      ...prhead,
                      vCAPNo: event.target.value,
                    });
                  }}
                  InputLabelProps={{ shrink: true }}
                />

                <TextField
                  className={classes.margin}
                  style={{ maxWidth: 150 }}
                  required
                  // disabled={editdisable}
                  disabled={true}
                  size="small"
                  id="vRequestor"
                  label="Requestor"
                  placeholder="Requestor"
                  variant="outlined"
                  value={prhead.vRequestor}
                  values={(values.vRequestor = prhead.vRequestor)}
                  InputLabelProps={{ shrink: true }}
                />

                <TextField
                  className={classes.margin}
                  style={{ maxWidth: 200 }}
                  // required
                  // disabled={editdisable}
                  disabled={true}
                  size="small"
                  id="vRemark"
                  label="Remark"
                  placeholder="Remark"
                  variant="outlined"
                  value={prhead.vRemark}
                  values={(values.vRemark = prhead.vRemark)}
                  onChange={(event) => {
                    // console.log(event.target.value);
                    setPRHead({
                      ...prhead,
                      vRemark: event.target.value,
                    });
                  }}
                  InputLabelProps={{ shrink: true }}
                />
              </Grid>
              <Grid container item xs className={classes.margin}>
                <TextField
                  className={classes.margin}
                  style={{ width: "200px" }}
                  required
                  error={true}
                  disabled={deliverydisable}
                  select
                  size="small"
                  variant="outlined"
                  margin="normal"
                  id="vDelivery"
                  label="Delivery terms"
                  value={prhead.vDelivery}
                  values={(values.vDelivery = prhead.vDelivery)}
                  onChange={(event) => {
                    // console.log(event.target.value);
                    setPRHead({
                      ...prhead,
                      vDelivery: event.target.value,
                    });
                  }}
                  InputLabelProps={{ shrink: true }}
                  SelectProps={{
                    native: true,
                  }}
                >
                  <option />
                  {deliverys.map((option) => (
                    <option key={option.ID} value={option.CTSTKY}>
                      {option.DELIVERY}
                    </option>
                  ))}
                </TextField>

                <TextField
                  className={classes.margin}
                  style={{ width: "200px" }}
                  required
                  error={true}
                  select
                  size="small"
                  variant="outlined"
                  margin="normal"
                  id="vPayment"
                  label="Payment terms"
                  value={prhead.vPayment}
                  values={(values.vPayment = prhead.vPayment)}
                  onChange={(event) => {
                    // console.log(event.target.value);
                    setPRHead({
                      ...prhead,
                      vPayment: event.target.value.trim(),
                    });
                  }}
                  InputLabelProps={{ shrink: true }}
                  SelectProps={{
                    native: true,
                  }}
                >
                  <option />
                  {payments.map((option) => (
                    <option key={option.ID} value={option.CTSTKY}>
                      {option.PAYMENT}
                    </option>
                  ))}
                </TextField>
              </Grid>
            </Paper>
          </Grid>
        </Grid>
      </form>
    );
  };

  const showRemark = () => {
    return (
      <form id="remarks">
        <TextField
          // required
          error={true}
          fullWidth
          // disabled="true"
          margin="dense"
          id="vNameRemarkDetail"
          label="PH Remark2"
          type="text"
          value={itemprdetail.vNameRemarkDetail}
          onChange={(event) => {
            // console.log(event.target.value);
            setItemPRDetail({
              ...itemprdetail,
              vNameRemarkDetail: event.target.value,
            });
          }}
        />
        <TextField
          // required
          error={true}
          fullWidth
          // disabled="true"
          margin="dense"
          id="vDescRemarkDetail"
          label="PH Remark3"
          type="text"
          value={itemprdetail.vDescRemarkDetail}
          onChange={(event) => {
            // console.log(event.target.value);
            setItemPRDetail({
              ...itemprdetail,
              vDescRemarkDetail: event.target.value,
            });
          }}
        />
        <TextField
          // required
          error={true}
          fullWidth
          // disabled="true"
          margin="dense"
          id="vTextRemarkDetail"
          label="Quotation"
          type="text"
          value={itemprdetail.vTextRemarkDetail}
          onChange={(event) => {
            // console.log(event.target.value);
            setItemPRDetail({
              ...itemprdetail,
              vTextRemarkDetail: event.target.value,
            });
          }}
        />
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
            PR Number : {prhead.vPRNumber}
            {itemprdetail.vItemLine
              ? ` - Line : ${itemprdetail.vItemLine}`
              : ""}
          </DialogTitle>
          <DialogContent>
            <Grid container item xs={12} spacing={2}>
              <Grid item xs={5}>
                <Autocomplete
                  // error
                  className={classes.margin}
                  autoFocus
                  required
                  fullWidth
                  disabled={editdisable}
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
                          /[^\w\s\ก-๙\-’/`~!#*$@_%+=.,^&(){}[\]|;:”"<>?\\]/g,
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

                      if (values.MMPUPR < 0) {
                        setSaveDisable(true);
                        setConfirmDisable(true);
                        alert("Price must be enter, Please contact ICT.");
                      }

                      if (values.MBORTY === "") {
                        setSaveDisable(true);
                        setConfirmDisable(true);
                        alert("Order Type must be enter, Please contact ICT.");
                      }

                      if (values.MMCUCD === "") {
                        setSaveDisable(true);
                        setConfirmDisable(true);
                        alert("Currency must be enter, Please contact ICT.");
                      }
                    }
                  }}
                  renderInput={(params) => (
                    <TextField
                      {...params}
                      // error={true}
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
                  // error={true}
                  fullWidth
                  disabled={editdisable}
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
                        /[^\w\s\ก-๙\-’/`~!#*$@_%+=.,^&(){}[\]|;:”"<>?\\]/g,
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
                  // error={true}
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
                  select
                  margin="dense"
                  variant="standard"
                  size="small"
                  required
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
                  InputLabelProps={{ shrink: true }}
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
              required
              error={true}
              fullWidth
              margin="dense"
              type="date"
              size="small"
              id="vDeliveryDate"
              label="Delivery Date"
              variant="standard"
              defaultValue={prhead.vDate}
              value={itemprdetail.vDateDetail}
              values={(values.vDateDetail = itemprdetail.vDateDetail)}
              onChange={(event) => {
                var dateNow = new Date();
                if (event.target.value < moment(dateNow).format("YYYY-MM-DD")) {
                  // alert("Date not less than present day.");
                } else {
                  setItemPRDetail({
                    ...itemprdetail,
                    vDateDetail: event.target.value,
                  });
                }
              }}
              InputLabelProps={{ shrink: true, required: true }}
            />
            <Grid container item xs={12} spacing={2}>
              <Grid item xs={5}>
                <Autocomplete
                  className={classes.margin}
                  autoFocus
                  required
                  fullWidth
                  size="small"
                  id="vSupplierNoAuto"
                  options={suppliers}
                  // getOptionLabel={(option) => option.IDSUNO}
                  // value={itemprdetail.vSupplierNo}
                  // values={(values.vSupplierNo = itemprdetail.vSupplierNo)}
                  getOptionLabel={(option) => option.SUPPLIER}
                  value={itemprdetail.vSupplierDesc}
                  values={(values.vSupplierNo = itemprdetail.vSupplierNo)}
                  onChange={(event, values) => {
                    // console.log(values);
                    if (values) {
                      setItemPRDetail({
                        ...itemprdetail,
                        // vSupplierNo: { IDSUNO: values.IDSUNO },
                        vSupplierNo: values.IDSUNO,
                        vSupplierName: values.SASUNM,
                        vSupplierDesc: { SUPPLIER: values.SUPPLIER },
                      });
                      // dispatch(itemunitActions.getItemUnits(values.IDSUNO));
                      // dispatch(
                      //   itemprdetailActions.getItems(
                      //     prhead.vWarehouse,
                      //     values.MMITNO
                      //   )
                      // );
                    }
                  }}
                  renderInput={(params) => (
                    <TextField
                      {...params}
                      error={true}
                      id="vSupplierNo"
                      label="Supplier No"
                      required
                      InputLabelProps={{ shrink: true }}
                    />
                  )}
                />
              </Grid>
              <Grid item xs>
                <TextField
                  // required
                  fullWidth
                  disabled="true"
                  margin="dense"
                  id="vSupplierName"
                  label="Supplier Name"
                  type="text"
                  value={itemprdetail.vSupplierName}
                  values={(values.vSupplierName = itemprdetail.vSupplierName)}
                  InputLabelProps={{ shrink: true }}
                />
              </Grid>
            </Grid>
            <Grid container item xs={12} spacing={2}>
              <Grid item xs={4}>
                <TextField
                  required
                  // error={true}
                  fullWidth
                  disabled={editdisable}
                  margin="dense"
                  id="vPrice"
                  label="Price"
                  type="number"
                  value={itemprdetail.vPrice}
                  values={(values.vPrice = itemprdetail.vPrice)}
                  InputLabelProps={{ shrink: true }}
                  onChange={(event) => {
                    // console.log(event.target.value);
                    let price = event.target.value;
                    let qty = itemprdetail.vQty;
                    setItemPRDetail({
                      ...itemprdetail,
                      vPrice: event.target.value,
                      vTotal: (qty * price).toFixed(4),
                    });
                  }}
                />
              </Grid>
              <Grid item xs>
                <TextField
                  required
                  // error={true}
                  fullWidth
                  disabled={editdisable}
                  margin="dense"
                  id="vVat"
                  label="Vat"
                  type="number"
                  value={itemprdetail.vVat}
                  values={(values.vVat = itemprdetail.vVat)}
                  InputLabelProps={{ shrink: true }}
                  onChange={(event) => {
                    // console.log(event.target.value);
                    setItemPRDetail({
                      ...itemprdetail,
                      vVat: event.target.value,
                    });
                  }}
                />
              </Grid>
              <Grid item xs>
                <TextField
                  required
                  fullWidth
                  disabled={editdisable}
                  margin="dense"
                  id="vCurrency"
                  label="Currency"
                  type="text"
                  value={itemprdetail.vCurrency}
                  values={(values.vCurrency = itemprdetail.vCurrency)}
                  InputLabelProps={{ shrink: true }}
                />
              </Grid>
              <Grid item xs>
                <TextField
                  required
                  // error={true}
                  fullWidth
                  disabled={editdisable}
                  margin="dense"
                  id="vOrderType"
                  label="Order Type"
                  type="number"
                  value={itemprdetail.vOrdertype}
                  values={(values.vOrdertype = itemprdetail.vOrdertype)}
                  InputLabelProps={{ shrink: true }}
                  // onInput={(e) => {
                  //   e.target.value = Math.max(0, parseInt(e.target.value))
                  //     .toString()
                  //     .slice(0, 3);
                  // }}
                  onChange={(event) => {
                    // console.log(event.target.value);

                    // if (
                    //   event.target.value == "705" ||
                    //   event.target.value == "707" ||
                    //   event.target.value == "708" ||
                    //   event.target.value == "713"
                    // ) {
                    //   // console.log("true");
                    //   setShowRemark(true);
                    // } else {
                    //   setShowRemark(false);
                    // }

                    setItemPRDetail({
                      ...itemprdetail,
                      vOrdertype: event.target.value,
                    });
                  }}
                />
              </Grid>
            </Grid>
            {/* <TextField
              required
              fullWidth
              disabled="true"
              margin="dense"
              id="vTotal"
              label="Total"
              type="number"
              value={itemprdetail.vTotal}
              values={(values.vTotal = itemprdetail.vTotal)}
            /> */}
            <TextField
              required
              fullWidth
              disabled={editdisable}
              margin="dense"
              id="vTotal"
              label="Total"
              value={itemprdetail.vTotal}
              values={(values.vTotal = itemprdetail.vTotal)}
              InputLabelProps={{ shrink: true }}
              InputProps={{
                inputComponent: NumberFormatCustom,
              }}
            />
            {/* <Grid container item xs={12} spacing={2}>
              <Grid item xs={5}>
                <TextField
                  className={classes.margin}
                  error={true}
                  disabled={editdisable}
                  fullWidth
                  required
                  select
                  margin="dense"
                  variant="standard"
                  size="small"
                  required
                  id="vPHGroup"
                  label="PH Group"
                  value={itemprdetail.vPHGroupDetail}
                  values={(values.vPHGroupDetail = itemprdetail.vPHGroupDetail)}
                  onChange={(event) => {
                    // console.log(event.target.value);
                    let phgroup = "PH";
                    setItemPRDetail({
                      ...itemprdetail,
                      vPHGroupDetail: event.target.value,
                    });
                    dispatch(
                      phbuyerActions.getPHBuyers(phgroup, event.target.value)
                    );
                  }}
                  InputLabelProps={{ shrink: true }}
                  SelectProps={{
                    native: true,
                  }}
                >
                  <option />
                  {phgroups.map((option) => (
                    <option key={option.ID} value={option.US_GRP}>
                      {option.US_GRP}
                    </option>
                  ))}
                </TextField>
              </Grid>
              <Grid item xs>
                <TextField
                  className={classes.margin}
                  error={true}
                  disabled={editdisable}
                  fullWidth
                  required
                  select
                  margin="dense"
                  variant="standard"
                  size="small"
                  required
                  id="vBuyer"
                  label="Buyer"
                  value={itemprdetail.vBuyerDetail}
                  values={(values.vBuyerDetail = itemprdetail.vBuyerDetail)}
                  onChange={(event) => {
                    // console.log(event.target.value);
                    setItemPRDetail({
                      ...itemprdetail,
                      vBuyerDetail: event.target.value,
                    });
                  }}
                  InputLabelProps={{ shrink: true }}
                  SelectProps={{
                    native: true,
                  }}
                >
                  <option />
                  {phbuyers.map((option) => (
                    <option key={option.ID} value={option.US_LOGIN}>
                      {option.US_LOGIN}
                    </option>
                  ))}
                </TextField>
              </Grid>
            </Grid>

            <TextField
              className={classes.margin}
              error={true}
              disabled={editdisable}
              fullWidth
              required
              select
              margin="dense"
              variant="standard"
              size="small"
              required
              id="vCostcenter"
              label="Cost center"
              value={itemprdetail.vCostcenterDetail}
              values={
                (values.vCostcenterDetail = itemprdetail.vCostcenterDetail)
              }
              onChange={(event) => {
                // console.log(event.target.value);
                setItemPRDetail({
                  ...itemprdetail,
                  vCostcenterDetail: event.target.value,
                });
              }}
              InputLabelProps={{ shrink: true }}
              SelectProps={{
                native: true,
              }}
            >
              <option />
              {costcenters.map((option) => (
                <option key={option.ID} value={option.S2AITM}>
                  {option.COSTCENTER}
                </option>
              ))}
            </TextField> */}
            <TextField
              // required
              error={true}
              fullWidth
              // disabled="true"
              margin="dense"
              id="vRemarkDetail"
              label="Remark"
              type="text"
              value={itemprdetail.vRemarkDetail}
              values={(values.vRemarkDetail = itemprdetail.vRemarkDetail)}
              onChange={(event) => {
                // console.log(event.target.value);
                setItemPRDetail({
                  ...itemprdetail,
                  vRemarkDetail: event.target.value,
                });
              }}
            />
            <TextField
              // required
              error={true}
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

            {showremark ? showRemark() : null}
            {/* {showremark ? <Remarks /> : null} */}
          </DialogContent>

          <DialogActions>
            <div>
              <Button
                startIcon={
                  showremark ? <ArrowDropUpIcon /> : <ArrowDropDownIcon />
                }
                onClick={() => {
                  showremark ? setShowRemark(false) : setShowRemark(true);
                }}
                color="default"
              />

              <Button onClick={handleClose} color="default">
                Close
              </Button>
              <Button
                disabled={savedisable}
                type="submit"
                color="primary"
                onClick={(event) => {
                  values.vNameRemarkDetail = itemprdetail.vNameRemarkDetail;
                  values.vDescRemarkDetail = itemprdetail.vDescRemarkDetail;
                  values.vTextRemarkDetail = itemprdetail.vTextRemarkDetail;

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
      editable: "never",
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
      field: "PR_PHREMARK1",
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
          {item.PR_PHREMARK1}
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
            prhead.vRequestor
              ? prhead.vRequestor
              : loginActions.getTokenUsername()
          );
          formData.append("vRemark", values.vRemark);
          formData.append("vApprove1", values.vApprove1);
          formData.append("vApprove2", values.vApprove2);
          formData.append("vStatus", prhead.vStatus ? prhead.vStatus : "00");

          let status = "92";
          dispatch(
            genpoActions.genPONumber(
              status,
              prnumber.vPRSelectNumberLine,
              prhead.vDelivery ? prhead.vDelivery : "null",
              prhead.vPayment
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
      {/* <p>#Debug {JSON.stringify(prhead)}</p> */}
      <MaterialTable
        id="root_pr"
        title={`Gen PO : ${prhead.vStatus}`}
        columns={columns}
        data={prdetailbuyerReducer.result ? prdetailbuyerReducer.result : []}
        // isLoading={prdetailbuyerReducer.result ? false : true}
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
        actions={[
          (rowData) => ({
            icon: "edit",
            tooltip: "Edit row",
            iconProps: { color: "secondary" },
            onClick: (event, rowData) => {
              console.log("rowData: " + JSON.stringify([rowData]));
              const index = rowData.tableData.id;
              let data = [rowData];
              let phgroup = "PH";
              data.map((item) => {
                dispatch(itemunitActions.getItemUnits(item.PR_IBITNO));
              });

              setTimeout(() => {
                data.map((item) => {
                  if (
                    item.PR_IBITNO.substr(0, 2) === "OH" ||
                    item.PR_IBITNO.substr(0, 2) === "BU" ||
                    item.PR_IBITNO.substr(0, 2) === "EL"
                  ) {
                    setEditNameDisable(false);
                  } else {
                    setEditNameDisable(true);
                  }

                  if (item.PR_IBPLPS === "") {
                    setConfirmDisable(true);
                  } else {
                    setConfirmDisable(false);
                  }

                  // if (
                  //   item.PR_IBORTY == "705" ||
                  //   item.PR_IBORTY == "707" ||
                  //   item.PR_IBORTY == "708" ||
                  //   item.PR_IBORTY == "713"
                  // ) {
                  //   // console.log("true");
                  //   setShowRemark(true);
                  // } else {
                  //   setShowRemark(false);
                  // }

                  setItemPRDetail({
                    ...itemprdetail,
                    id: index,
                    vItemLine: item.PR_IBPLPS,
                    // vItemNo: { MMITNO: item.PR_IBITNO },
                    vItemNo: item.PR_IBITNO,
                    vItemDesc1: item.PR_IBPITT,
                    vItemDesc2: { ITEM: item.ITEM },
                    vQty: item.PR_IBORQA,
                    vUnit: item.PR_IBPUUN,
                    vDateDetail: item.PR_IBDWDT,
                    // vSupplierNo: { IDSUNO: item.PR_IBSUNO },
                    vSupplierNo: item.PR_IBSUNO,
                    vSupplierName: item.SASUNM,
                    vSupplierDesc: { SUPPLIER: item.SUPPLIER },
                    vPrice: item.PR_IBPUPR,
                    vVat: item.PR_IBVTCD,
                    vCurrency: item.PR_IBCUCD,
                    vOrdertype: item.PR_IBORTY,
                    vTotal: (item.PR_IBORQA * item.PR_IBPUPR).toFixed(4),
                    vCostcenterDetail: item.PR_IBCOCE,
                    vPHGroupDetail: item.PR_IBMODL,
                    vBuyerDetail: item.PR_IBBUYE,
                    vRemarkDetail: item.PR_REM3,
                    vPHRemarkDetail: item.PR_PHREMARK1,
                    vNameRemarkDetail: item.PR_PHREMARK2,
                    vDescRemarkDetail: item.PR_PHREMARK3,
                    vTextRemarkDetail: item.PR_PHREMARK4,
                  });
                });
              }, 500);

              setSelectedProduct("rowData");
              setAddFreeItem(true);
              setEditDisable(true);
              setOpenDialog(true);
            },
          }),
        ]}
      />

      {/* Dialog */}
      <Formik
        initialValues={{
          vPRNumber: prhead.vPRNumber,
          vPlanUnPlan: prhead.vPlanUnPlan,
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
          formData.append("vPRNumber", prhead.vPRNumber);
          formData.append(
            "vPlanUnPlan",
            prhead.vPlanUnPlan === "Plan" ? "5" : "6"
          );
          formData.append("vItemLine", itemprdetail.vItemLine);
          formData.append("vItemNo", values.vItemNo);
          formData.append("vItemDesc1", values.vItemDesc1);
          formData.append("vQty", values.vQty);
          formData.append("vUnit", values.vUnit);
          formData.append("vDateDetail", values.vDateDetail);
          formData.append("vSupplierNo", values.vSupplierNo);
          formData.append("vPrice", values.vPrice);
          formData.append("vVat", values.vVat);
          formData.append("vCurrency", values.vCurrency);
          formData.append("vOrdertype", values.vOrdertype);
          formData.append("vTotal", values.vTotal);
          formData.append("vCostcenterDetail", prhead.vCostcenter);
          formData.append("vPHGroupDetail", prhead.vPHGroup);
          formData.append("vBuyerDetail", prhead.vBuyer);
          formData.append("vRemarkDetail", values.vRemarkDetail);
          formData.append("vPHRemarkDetail", values.vPHRemarkDetail);
          formData.append("vNameRemarkDetail", values.vNameRemarkDetail);
          formData.append("vDescRemarkDetail", values.vDescRemarkDetail);
          formData.append("vTextRemarkDetail", values.vTextRemarkDetail);
          formData.append("vAddFreeItem", itemprdetail.vAddFreeItem);
          formData.append("vConfirm", "1");
          formData.append("vStatus", "10");

          setSaveDisable(true);
          setConfirmDisable(true);
          setSuccess(false);
          setLoading(true);

          if (update) {
            // console.log("update");
            let fromStatus = "92";
            dispatch(
              prdetailbuyerActions.updateEPRDetailupdateEPRDetailGenPO(
                formData,
                props.history,
                prhead.vPRNumber,
                fromStatus,
                prnumber.vPRSelectNumberLine
              )
            );
            setTimeout(() => {
              setItemPRDetail({ ...initialStateItemPRDetail });
              // dispatch(prdetailbuyerActions.getEPRDetails(prhead.vPRNumber));
              setOpenDialog(false);
              setAddFreeItem(false);
              setConfirmDisable(false);
              setUpdate(false);
              setSaveDisable(false);
              setSuccess(true);
              setLoading(false);
            }, 500);
          }
        }}
      >
        {(props) => showDialog(props)}
      </Formik>
    </div>
  );
};
