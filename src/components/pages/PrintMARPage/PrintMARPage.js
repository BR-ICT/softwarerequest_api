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
import AttachFileIcon from "@material-ui/icons/AttachFile";
import GetAppIcon from "@material-ui/icons/GetApp";
import CircularProgress from "@material-ui/core/CircularProgress";
import KeyboardArrowDownIcon from "@material-ui/icons/KeyboardArrowDown";
import KeyboardArrowUpIcon from "@material-ui/icons/KeyboardArrowUp";
import { Formik, Form, Field } from "formik";
import {
  red,
  green,
  purple,
  teal,
  deepOrange,
  blueGrey,
  yellow,
} from "@material-ui/core/colors/";
import * as loginActions from "./../../../actions/login.action";
import * as marnumberActions from "./../../../actions/marnumber.action";
import * as marheadActions from "./../../../actions/marhead.action";
import * as mardetailActions from "./../../../actions/mardetail.action";
import * as marfileActions from "./../../../actions/marfile.action";
import * as buActions from "./../../../actions/bu.action";
import * as costcenterActions from "./../../../actions/costcenter.action";
import * as warehouseActions from "./../../../actions/warehouse.action";
import * as approveActions from "./../../../actions/approve.action";
import * as itemActions from "./../../../actions/item.action";
import * as itemdetialActions from "./../../../actions/itemdetail.action";
import * as accountantActions from "./../../../actions/accountant.action";

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

export default (props) => {
  const classes = useStyles();
  const dispatch = useDispatch();
  const marheadReducer = useSelector(({ marheadReducer }) => marheadReducer);
  const mardetailReducer = useSelector(
    ({ mardetailReducer }) => mardetailReducer
  );
  const marnumberReducer = useSelector(
    ({ marnumberReducer }) => marnumberReducer
  );
  const marfileReducer = useSelector(({ marfileReducer }) => marfileReducer);
  const itemReducer = useSelector(({ itemReducer }) => itemReducer);
  const itemdetailReducer = useSelector(
    ({ itemdetailReducer }) => itemdetailReducer
  );
  const accountantReducer = useSelector(
    ({ accountantReducer }) => accountantReducer
  );
  const buReducer = useSelector(({ buReducer }) => buReducer);
  const costcenterReducer = useSelector(
    ({ costcenterReducer }) => costcenterReducer
  );
  const warehouseReducer = useSelector(
    ({ warehouseReducer }) => warehouseReducer
  );
  const approveReducer = useSelector(({ approveReducer }) => approveReducer);

  const initialStateMARHead = {
    vMARNumber: "",
    vDate: moment(new Date()).format("YYYY-MM-DD"),
    vPostDate: moment(new Date()).format("YYYY-MM-DD"),
    vMonth: moment(new Date()).format("YYYYMM"),
    vType: "",
    vPrefix: "A",
    vBU: "",
    vCostcenter: "",
    vCostcenterDesc: "",
    vAccountant: "",
    vRequestor: loginActions.getTokenUsername(),
    vPurpose: "",
    vRemark: "",
    vAccRemark: "",
    vApprove1: "",
    vApprove2: "",
    vApprove3: "RUNGRA_JIN",
    vApprove4: "WEERAS_WAH",
    vApprove5: "ROSANN_SUC",
    vAppWHS: "",
    vAppICT: "THIPSU_THI",
    vAppCIO: "WUTINA_ULI",
    vStatus: "",
    vFromStatus: "",
    vToStatus: "",
    vReason: "",
  };
  const [marhead, setMARHead] = useState(initialStateMARHead);
  const [mardetail, setMARDetail] = useState(null);
  const [marnumber, setMARNumber] = useState({
    vMARSelectNumber: "",
    vMARSelectNumberLine: "",
  });
  const [marfile, setMARFile] = useState(null);
  const initialStateFileMARDetail = {
    vMARNumber: "",
    vPrefix: "",
    vFileLine: "",
    vFile: "",
    vFileName: "",
    vFlieType: "",
    vFliePath: "",
  };
  const [filemardetail, setFileMARDetail] = useState(initialStateFileMARDetail);
  const [itemdetail, setItemDetail] = useState(null);
  const initialStateItemMARDetail = {
    vMARNumber: "",
    vPrefix: "",
    vRefNumber: "",
    vTypeAdjust: "0",
    vItemLine: "",
    vItem: "",
    vItemDesc1: "",
    vItemDesc2: null,
    vWarehouse: "",
    vWarehouseDesc1: "",
    vWarehouseDesc2: null,
    vLocation: "",
    vLotNo: "",
    vDate: "",
    vUnit: "",
    vQty: "",
    vUnitPrice: "",
    vAmount: "",
    vRemark1: "",
    vRemark2: "",
    vStatus: "",
  };
  const [itemmardetail, setItemMARDetail] = useState(initialStateItemMARDetail);

  const [searchdisable, setSearchDisable] = useState(false);
  const [newdisable, setNewDisable] = useState(false);
  const [editdisable, setEditDisable] = useState(true);
  const [canceldisable, setCancelDisable] = useState(true);
  const [allocatedisable, setAllocateDisable] = useState(true);
  const [savedisable, setSaveDisable] = useState(true);
  const [cleardisable, setClearDisable] = useState(true);
  const [submitdisable, setSubmitDisable] = useState(true);
  const [imagereqdisable, setImageReqDisable] = useState(true);
  const [additemdisable, setAddItemDisable] = useState(true);
  const [editnamedisable, setEditNameDisable] = useState(true);
  const [create, setCreate] = useState(true);
  const [update, setUpdate] = useState(false);
  const [reject, setReject] = useState(false);
  const [openDialog, setOpenDialog] = useState(false);
  const [openDialogFile, setOpenDialogFile] = useState(false);
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [loading, setLoading] = useState(false);
  const [loadingallocate, setLoadingAllocate] = useState(false);
  const [loadingcancel, setLoadingCancel] = useState(false);
  const [success, setSuccess] = useState(false);
  const [file, setFile] = useState(false);
  const inputRef = useRef(null);
  const [showfile, setShowFile] = useState(true);
  const [purpose, setPurpose] = useState([]);
  const [cooapprove, setCOOApprove] = useState(false);
  const [ceoapprove, setCEOApprove] = useState(false);
  const [whsapprove, setWHSApprove] = useState(false);

  useEffect(() => {
    marheadReducer.result = null;
    mardetailReducer.result = null;
    marfileReducer.result = null;
    (async function() {
      let fromStatus = "50";
      let toStatus = "50";
      await dispatch(
        marnumberActions.getMARNumber(
          initialStateMARHead.vPrefix,
          fromStatus,
          toStatus
        )
      );
    })();
  }, []);

  useEffect(() => {
    const marheads = marheadReducer.result ? marheadReducer.result : [];

    marheads.map((item) => {
      // console.log(item);
      dispatch(costcenterActions.getCostCentersBU(item.MHBU));

      if (item.MHBU === "BU01" || item.MHBU === "BU02") {
        setPurpose(purposes1);
      } else {
        setPurpose(purposes2);
      }

      handleCheckCOOApprove_MARA(item.MHBU);

      setMARHead({
        ...marhead,
        vMARNumber: item.MARNUMBER,
        vDate: item.MHREDA,
        vPostDate: item.MHPODA,
        vMonth: item.MHMONT,
        vType: item.MHTYPE,
        vPrefix: item.MHPREF,
        vBU: item.MHBU,
        vCostcenter: item.MHCOCE,
        vCostcenterDesc: item.COSTCENTER,
        vAccountant: item.MHACCT,
        vRequestor: item.MHREQU,
        vRemark: item.MHREM1,
        vPurpose: item.MHREM2,
        vApprove1: item.MHAPP1,
        vApprove2: item.MHAPP2,
        vApprove3: item.MHAPP3,
        vApprove4: item.MHAPP4,
        vApprove5: item.MHAPP5,
        vAppWHS: item.MHAPWHS,
        vAppICT: item.MHAPICT,
        vAppCIO: item.MHAPCIO,
        vStatus: item.MHSTAT,
      });
    });
  }, [marheadReducer]);

  useEffect(() => {
    const mardetails = mardetailReducer.result ? mardetailReducer.result : [];
    setMARDetail(mardetails);
    handleCheckWHSApprove(mardetails);
    handleCheckSubmit(mardetails);
  }, [mardetailReducer]);

  useEffect(() => {
    setMARFile(marfileReducer.result ? marfileReducer.result : []);
  }, [marfileReducer]);

  useEffect(() => {
    setItemDetail(itemdetailReducer.result ? itemdetailReducer.result : []);
  }, [itemdetailReducer]);

  const marnumbers = useMemo(() =>
    marnumberReducer.result ? marnumberReducer.result : []
  );

  const accountants = useMemo(() =>
    accountantReducer.result ? accountantReducer.result : []
  );

  const bus = useMemo(() => (buReducer.result ? buReducer.result : []));

  const costcenters = useMemo(() =>
    costcenterReducer.result ? costcenterReducer.result : []
  );

  const warehouses = useMemo(() =>
    warehouseReducer.result ? warehouseReducer.result : []
  );

  const approves = useMemo(() =>
    approveReducer.result ? approveReducer.result : []
  );

  const items = useMemo(() => (itemReducer.result ? itemReducer.result : []));

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

  const ColorButtonYellow = withStyles((theme) => ({
    root: {
      color: theme.palette.getContrastText(yellow[500]),
      backgroundColor: yellow[500],
      "&:hover": {
        backgroundColor: yellow[700],
      },
    },
  }))(Button);

  const handleSearch = (marnumber = "") => {
    if (marnumber !== "") {
      setCreate(false);
      setSearchDisable(false);
      setNewDisable(true);
      setSaveDisable(false);
      setClearDisable(false);
      setCancelDisable(false);
      setAllocateDisable(false);
      setSubmitDisable(false);
      setEditDisable(false);
      setAddItemDisable(false);

      // let fromStatus = "00";
      // let toStatus = "05";
      // dispatch(marheadActions.getMARHead(marnumber, fromStatus, toStatus));
      // dispatch(mardetailActions.getMARDetail(marnumber, fromStatus, toStatus));
      dispatch(marfileActions.getMARFile(marnumber));
    } else {
      marheadReducer.result = null;
      mardetailReducer.result = null;
      marfileReducer.result = null;

      setMARHead({
        ...initialStateMARHead,
      });
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
    marheadReducer.result = null;
    mardetailReducer.result = null;
    marfileReducer.result = null;

    setMARNumber({ ...marnumber, vMARSelectNumber: "" });
    setMARHead({ ...initialStateMARHead });
    setMARDetail([]);
    setMARFile([]);
    setPurpose([]);
    setItemMARDetail({ ...initialStateItemMARDetail });

    setCreate(true);
    setSearchDisable(false);
    setNewDisable(false);
    setSaveDisable(true);
    setClearDisable(true);
    setCancelDisable(true);
    setAllocateDisable(true);
    setSubmitDisable(true);
    setEditDisable(true);
    setAddItemDisable(true);

    setCOOApprove(false);
    setCEOApprove(false);
    setWHSApprove(false);
  };

  const handleClearBU = () => {
    costcenterReducer.result = null;
    setMARHead({
      ...marhead,
      vBU: "",
      vCostcenter: "",
      vCostcenterDesc: "",
      vPurpose: "",
      vApprove1: "",
      vApprove2: "",
      vApprove3: "",
      vApprove4: "",
      vApprove5: "",
    });
    setPurpose([]);
    setCOOApprove(false);
    setCEOApprove(false);
  };

  const handleDialogClose = () => {
    itemReducer.result = null;
    itemdetailReducer.result = null;
    setOpenDialog(false);
    setItemMARDetail(initialStateItemMARDetail);

    (async function() {
      let fromStatus = "00";
      let toStatus = "05";
      await dispatch(
        mardetailActions.fetchMARDetail(
          marhead.vMARNumber,
          fromStatus,
          toStatus
        )
      );
    })();
  };

  const handleDialogFileClose = () => {
    setOpenDialogFile(false);
    resetFileInput();
  };

  const handleCheckCOOApprove_MARA = (bu) => {
    // console.log(bu);
    if (bu === "BU01" || bu === "BU02") {
      //BU01, BU02
      setCOOApprove(true);
    } else {
      setCOOApprove(false);
    }
  };

  const handleCheckWHSApprove = (mardetail) => {
    // console.log(mardetail);
    if (mardetail.length) {
      mardetail.map((item) => {
        let count = 0;
        if (item.MLWHLO === "A91" || item.MLWHLO === "A93") {
          count += 1;
        }

        if (count > 0) {
          setWHSApprove(true);
        } else {
          setWHSApprove(false);
        }
      });
    } else {
      setWHSApprove(false);
    }
  };

  const handleCheckSubmit = (mardetail) => {
    // console.log(mardetail);
    if (mardetail.length) {
      let count = 0;
      mardetail.map((item) => {
        if (item.MLTYPE === "0") {
          if (item.MLRENO === "") {
            count += 1;
          } else {
            if (
              item.MGTRSL.substring(0, 1) === "1" ||
              item.MGTRSL.substring(0, 1) === "2"
            ) {
              count += 1;
            }
          }
        }
      });
      // console.log("count: " + count);
      if (count > 0) {
        setSubmitDisable(true);
      } else {
        setSubmitDisable(false);
      }
    } else {
      setSubmitDisable(true);
    }
  };

  const handleCheckApproveDuplicate = (
    approve1,
    approve2,
    approve3,
    approve4,
    approve5
  ) => {
    // console.log(`${approve1} ${approve2} ${approve3} ${approve4} ${approve5}`);
    let check = true;
    if (approve1) {
      // console.log("vApprove1");
      if (
        approve1 === marhead.vApprove2 ||
        approve1 === marhead.vApprove3 ||
        approve1 === marhead.vApprove4 ||
        approve1 === marhead.vApprove5
      ) {
        // return false;
        check = false;
      }
    }
    if (approve2) {
      // console.log("vApprove2");
      if (
        approve2 === marhead.vApprove1 ||
        approve2 === marhead.vApprove3 ||
        approve2 === marhead.vApprove4 ||
        approve2 === marhead.vApprove5
      ) {
        // return false;
        check = false;
      }
    }
    if (approve3) {
      // console.log("vApprove3");
      if (
        approve3 === marhead.vApprove1 ||
        approve3 === marhead.vApprove2 ||
        approve3 === marhead.vApprove4 ||
        approve3 === marhead.vApprove5
      ) {
        // return false;
        check = false;
      }
    }
    if (approve4) {
      // console.log("vApprove4");
      if (
        approve4 === marhead.vApprove1 ||
        approve4 === marhead.vApprove2 ||
        approve4 === marhead.vApprove3 ||
        approve4 === marhead.vApprove5
      ) {
        // return false;
        check = false;
      }
    }
    if (approve5) {
      // console.log("vApprove5");
      if (
        approve5 === marhead.vApprove1 ||
        approve5 === marhead.vApprove2 ||
        approve5 === marhead.vApprove3 ||
        approve5 === marhead.vApprove4
      ) {
        // return false;
        check = false;
      }
    }
    // console.log("check: " + check);
    return check;
  };

  const resetFileInput = () => {
    // 👇️ reset input value
    inputRef.current.value = null;
    setFile(false);
  };

  const types = [
    {
      ID: "0",
      TYPE: "Adjust Stock",
    },
    // {
    //   ID: "1",
    //   TYPE: "Cancel GRN",
    // },
  ];

  const purposes1 = [
    {
      ID: "0",
      PURPOSE: "Stock count",
    },
    {
      ID: "1",
      PURPOSE: "Casava / Corn",
    },
    {
      ID: "2",
      PURPOSE: "Adj. DOD import",
    },
    {
      ID: "3",
      PURPOSE: "Cut lot",
    },
    {
      ID: "4",
      PURPOSE: "Adj. pro-rata import",
    },
    {
      ID: "5",
      PURPOSE: "Adj. unloading / transportation fee to RM cost",
    },
    {
      ID: "6",
      PURPOSE: "Other",
    },
  ];

  const purposes2 = [
    {
      ID: "0",
      PURPOSE: "Stock count",
    },
    {
      ID: "1",
      PURPOSE: "Reclass item for sales",
    },
    {
      ID: "2",
      PURPOSE: "Expried inventory",
    },
    {
      ID: "3",
      PURPOSE: "The wrong spec product",
    },
    {
      ID: "4",
      PURPOSE: "Wrong vat",
    },
    {
      ID: "5",
      PURPOSE: "Other",
    },
  ];

  const typeadjusts = [
    {
      ID: "0",
      TYPE: "Adjust -",
    },
    {
      ID: "1",
      TYPE: "Adjust +",
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
              <Grid container item xs={12} style={{ marginBottom: 2 }}>
                <Grid item xs={12} md={3} className={classes.wrapper}>
                  <TextField
                    fullWidth
                    select
                    size="small"
                    variant="outlined"
                    id="vSelectMARNumber"
                    label="MAR-A Number"
                    disabled={searchdisable}
                    value={marnumber.vMARSelectNumber}
                    onChange={(event) => {
                      // console.log(event.target.value);
                      if (event.target.value) {
                        setMARNumber({
                          ...marnumber,
                          vMARSelectNumber: event.target.value,
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
                    {marnumbers.map((option) => (
                      <option key={option.ID} value={option.MARNUMBER}>
                        {option.MARNUMBER}
                      </option>
                    ))}
                  </TextField>
                </Grid>
                <Grid item xs={6} md={"auto"} className={classes.wrapper}>
                  <a
                    href={`${
                      process.env.REACT_APP_API_URL
                    }/mar_api/report/viewmarwotoken/${loginActions.getTokenCono()}/${loginActions.getTokenDivi()}/${
                      marnumber.vMARSelectNumber
                    }`}
                    target="_blank"
                    style={{ textDecoration: "none" }}
                  >
                    {/* <a
                    href={`${process.env.REACT_APP_API_URL}/mar_api/report/viewmar/${marnumber.vMARSelectNumber}`}
                    target="_blank"
                    style={{ textDecoration: "none" }}
                  > */}
                    <Button
                      fullWidth
                      variant="contained"
                      color="primary"
                      // disabled={viewMPRDisable}
                      startIcon={<SearchIcon />}
                    >
                      View MAR
                    </Button>
                  </a>
                </Grid>
                <Grid item xs={6} md={"auto"} className={classes.wrapper}>
                  <ColorButton
                    fullWidth
                    size="medium"
                    id="vSave"
                    variant="contained"
                    color="primary"
                    type="submit"
                    startIcon={<SendIcon />}
                    disabled={submitdisable}
                    onClick={(event) => {
                      values.vSubmit = "approve";
                      values.vStatus = "70";
                      values.vFromStatus = "50";
                      values.vToStatus = "70";
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
                <Grid item xs={6} md={"auto"} className={classes.wrapper}>
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

              {showfile ? showFile() : null}

              <Grid container item xs={12} spacing={1} justify="center">
                <Button
                  size="large"
                  startIcon={
                    showfile ? (
                      <KeyboardArrowUpIcon />
                    ) : (
                      <KeyboardArrowDownIcon />
                    )
                  }
                  onClick={() => {
                    showfile ? setShowFile(false) : setShowFile(true);
                  }}
                  color="default"
                />
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
        // style={{ maxWidth: "800px" }} //{ padding: "0px 10px" }
        maxWidth="lg"
        keepMounted
        onClose={() => {}}
        aria-labelledby="alert-dialog-slide-title"
        aria-describedby="alert-dialog-slide-description"
      >
        <form onSubmit={handleSubmit}>
          <DialogTitle id="alert-dialog-slide-title">
            Item Detail : {marhead.vMARNumber}
            {itemmardetail.vItemLine
              ? ` - Line : ${itemmardetail.vItemLine}`
              : ""}
          </DialogTitle>
          <DialogContent>
            <Grid container item xs={12} md={"auto"} spacing={2}>
              <Grid item xs={2}>
                <TextField
                  className={classes.margin}
                  // disabled={create ? editdisable : true}
                  fullWidth
                  required
                  error
                  select
                  size="small"
                  id="vTypeAdjust"
                  label="Type Adjust"
                  placeholder="Type Adjust"
                  value={itemmardetail.vTypeAdjust}
                  values={(values.vTypeAdjust = itemmardetail.vTypeAdjust)}
                  onChange={(event) => {
                    // console.log(event.target.value);
                    if (event.target.value === "1") {
                      itemdetailReducer.result = null;
                      setItemDetail([]);
                    }

                    if (itemmardetail.vWarehouse) {
                      dispatch(
                        itemActions.getItems(
                          itemmardetail.vWarehouse,
                          event.target.value
                        )
                      );
                    }

                    setItemMARDetail({
                      ...itemmardetail,
                      vTypeAdjust: event.target.value,
                      vDate: moment(new Date()).format("YYYY-MM-DD"),
                      vLotNo: `ADJ.${marhead.vBU}${moment(new Date()).format(
                        "DDMMYY"
                      )}`,
                    });

                    // values.vDate = moment(new Date()).format("YYYY-MM-DD");
                    // values.vLotNo = `ADJ${moment(new Date()).format("YYMMDD")}`;
                  }}
                  InputLabelProps={{ shrink: true }}
                  SelectProps={{
                    native: true,
                  }}
                >
                  {/* <option /> */}
                  {typeadjusts.map((option) => (
                    <option key={option.ID} value={option.ID}>
                      {option.TYPE}
                    </option>
                  ))}
                </TextField>
              </Grid>
              <Grid item xs={3}>
                <Autocomplete
                  className={classes.margin}
                  // disabled={itemmardetail.vItemLine === "" ? false : true}
                  error
                  autoFocus
                  required
                  fullWidth
                  size="small"
                  id="vWarehouse"
                  options={warehouses}
                  getOptionLabel={(option) => option.WAREHOUSE}
                  value={itemmardetail.vWarehouseDesc2}
                  values={(values.vWarehouse = itemmardetail.vWarehouse)}
                  onChange={(event, values) => {
                    // console.log(values.MWWHLO);
                    if (values) {
                      setItemMARDetail({
                        ...itemmardetail,
                        vWarehouse: values.MWWHLO,
                        vWarehouseDesc1: values.MWWHNM,
                        vWarehouseDesc2: { WAREHOUSE: values.WAREHOUSE },
                      });

                      dispatch(
                        itemActions.getItems(
                          values.MWWHLO,
                          itemmardetail.vTypeAdjust
                        )
                      );
                    } else {
                      itemReducer.result = null;
                      itemdetailReducer.result = null;
                      setItemMARDetail({
                        ...itemmardetail,
                        vWarehouse: null,
                        vWarehouseDesc1: null,
                        vWarehouseDesc2: null,
                        vItem: null,
                        vItemDesc1: null,
                        vItemDesc2: null,
                      });
                    }
                  }}
                  renderInput={(params) => (
                    <TextField
                      {...params}
                      error={true}
                      id="vWarehouse"
                      label="Warehouse"
                      required
                      InputLabelProps={{ shrink: true }}
                    />
                  )}
                />
              </Grid>
              <Grid item xs={5}>
                <Autocomplete
                  className={classes.margin}
                  disabled={itemmardetail.vItemLine === "" ? false : true}
                  error
                  autoFocus
                  required
                  fullWidth
                  size="small"
                  id="vItemAuto"
                  options={items}
                  getOptionLabel={(option) => option.ITEM}
                  value={itemmardetail.vItemDesc2}
                  values={
                    ((values.vItem = itemmardetail.vItem),
                    (values.vItemDesc1 = values.MMFUDS))
                  }
                  onChange={(event, values) => {
                    // console.log(values);
                    if (values) {
                      setItemMARDetail({
                        ...itemmardetail,
                        vItem: values.MMITNO,
                        vItemDesc1: values.MMFUDS,
                        vItemDesc2: { ITEM: values.ITEM },
                        vUnit: values.MMUNMS,
                        vFacility: values.MBFACI,
                        // vWarehouse: values.MBWHLO,
                        vLocation: values.MBWHSL,
                      });

                      if (itemmardetail.vTypeAdjust === "0") {
                        dispatch(
                          itemdetialActions.getItemDetails(
                            itemmardetail.vWarehouse,
                            values.MMITNO
                          )
                        );
                      }
                    } else {
                      setItemMARDetail({
                        ...itemmardetail,
                        vItem: null,
                        vItemDesc1: null,
                        vItemDesc2: null,
                      });
                    }
                  }}
                  renderInput={(params) => (
                    <TextField
                      {...params}
                      error={true}
                      id="vItem"
                      label="Item No"
                      required
                      InputLabelProps={{ shrink: true }}
                    />
                  )}
                />
              </Grid>
              {/* <Grid item style={{ alignSelf: "center" }}>
                <Button
                  // style={{ alignSelf: "center" }}
                  // fullWidth
                  size="small"
                  id="vSearchItem"
                  variant="contained"
                  color="primary"
                  disabled={searchdisable}
                  startIcon={
                    itemmardetail.vTypeAdjust === "0" ? (
                      <SearchIcon />
                    ) : (
                      <AddIcon />
                    )
                  }
                  onClick={(event) => {
                    // handleSearch(marnumber.vMARSelectNumber);
                    if (itemmardetail.vTypeAdjust === "0") {
                      dispatch(
                        itemdetialActions.getItemDetails(
                          itemmardetail.vWarehouse,
                          itemmardetail.vItem
                        )
                      );
                    } else {
                      let formData = new FormData();
                      formData.append("vMARNumber", marhead.vMARNumber);
                      formData.append("vPrefix", marhead.vPrefix);
                      formData.append("vRefNumber", "");
                      formData.append("vTypeAdjust", itemmardetail.vTypeAdjust);
                      formData.append("vItemLine", "");
                      formData.append("vItem", itemmardetail.vItem);
                      formData.append("vItemDesc1", itemmardetail.vItemDesc1);
                      formData.append("vFacility", "1A1");
                      formData.append("vWarehouse", itemmardetail.vWarehouse);
                      formData.append("vLocation", "");
                      formData.append("vLotNo", "ADJ");
                      formData.append("vDate", "");
                      formData.append("vUnit", itemmardetail.vUnit);
                      formData.append("vQty", itemmardetail.vQty);
                      formData.append("vUnitPrice", "");
                      formData.append("vAmount", "");
                      formData.append("vRemark1", "");
                      formData.append("vRemark2", "");
                      formData.append("vStatus", "00");
                      (async function() {
                        await dispatch(mardetailActions.addMARDetail(formData));
                      })();
                    }
                  }}
                >
                  {itemmardetail.vTypeAdjust === "0" ? "Search" : "Create"}
                </Button>
              </Grid> */}
            </Grid>
            {itemmardetail.vTypeAdjust === "1" ? (
              <Grid container item xs style={{ marginBottom: 2 }} spacing={1}>
                <Grid item xs={5}>
                  <TextField
                    className={classes.margin}
                    disabled={itemmardetail.vTypeAdjust === "0" ? true : false}
                    fullWidth
                    required
                    error={itemmardetail.vTypeAdjust === "1" ? true : false}
                    size="small"
                    id="vQty"
                    label="Qty"
                    placeholder="Qty"
                    type="number"
                    value={itemmardetail.vQty}
                    values={(values.vQty = itemmardetail.vQty)}
                    onChange={(event) => {
                      // console.log(event.target.value);
                      setItemMARDetail({
                        ...itemmardetail,
                        vQty: event.target.value,
                      });
                    }}
                    InputLabelProps={{ shrink: true }}
                    SelectProps={{
                      native: true,
                    }}
                  ></TextField>
                </Grid>
                <Grid item xs={5}>
                  <TextField
                    className={classes.margin}
                    disabled={itemmardetail.vTypeAdjust === "0" ? true : false}
                    fullWidth
                    required
                    error={itemmardetail.vTypeAdjust === "1" ? true : false}
                    size="small"
                    id="vLotNo"
                    label="Lot No"
                    placeholder="Lot No"
                    value={itemmardetail.vLotNo}
                    values={(values.vLotNo = itemmardetail.vLotNo)}
                    onChange={(event) => {
                      // console.log(event.target.value);
                      setItemMARDetail({
                        ...itemmardetail,
                        vLotNo: event.target.value,
                      });
                    }}
                    InputLabelProps={{ shrink: true }}
                    SelectProps={{
                      native: true,
                    }}
                  ></TextField>
                </Grid>
                <Grid item style={{ alignSelf: "center" }}>
                  <Button
                    // style={{ alignSelf: "center" }}
                    // fullWidth
                    type="submit"
                    size="small"
                    id="vCreateItem"
                    variant="contained"
                    color="primary"
                    disabled={itemmardetail.vTypeAdjust === "0" ? true : false}
                    startIcon={<AddIcon />}
                    onClick={(event) => {
                      // let formData = new FormData();
                      // formData.append("vMARNumber", marhead.vMARNumber);
                      // formData.append("vPrefix", marhead.vPrefix);
                      // formData.append("vRefNumber", "");
                      // formData.append("vTypeAdjust", itemmardetail.vTypeAdjust);
                      // formData.append("vItemLine", "");
                      // formData.append("vItem", itemmardetail.vItem);
                      // formData.append("vItemDesc1", itemmardetail.vItemDesc1);
                      // formData.append("vFacility", itemmardetail.vFacility);
                      // formData.append("vWarehouse", itemmardetail.vWarehouse);
                      // formData.append("vLocation", itemmardetail.vLocation);
                      // formData.append("vLotNo", itemmardetail.vLotNo);
                      // formData.append("vDate", itemmardetail.vDate);
                      // formData.append("vUnit", itemmardetail.vUnit);
                      // formData.append("vQty", itemmardetail.vQty);
                      // formData.append("vUnitPrice", "0");
                      // formData.append("vAmount", "0");
                      // formData.append("vRemark1", "");
                      // formData.append("vRemark2", "");
                      // formData.append("vStatus", "00");
                      (async function() {
                        // await dispatch(mardetailActions.addMARDetail(formData));
                        // let fromStatus = "00";
                        // let toStatus = "05";
                        // await dispatch(
                        //   mardetailActions.fetchMARDetail(
                        //     marhead.vMARNumber,
                        //     fromStatus,
                        //     toStatus
                        //   )
                        // );
                        // handleDialogClose();
                      })();

                      // formData.append("vMARNumber", values.vMARNumber);
                      // formData.append("vPrefix", values.vPrefix);
                      // formData.append("vTypeAdjust", values.vTypeAdjust);
                      // formData.append("vItem", values.vItem);
                      // formData.append("vItemDesc1", values.vItemDesc1);
                      // formData.append("vFacility", values.vFacility);
                      // formData.append("vWarehouse", values.vWarehouse);
                      // formData.append("vLocation", values.vLocation);
                      // formData.append("vLotNo", values.vLotNo);
                      // formData.append("vDate", values.vDate);
                      // formData.append("vUnit", values.vUnit);
                      // formData.append("vQty", values.vQty);

                      values.vMARNumber = marhead.vMARNumber;
                      values.vPrefix = marhead.vPrefix;
                      values.vItemDesc = itemmardetail.vItemDesc1;
                      values.vUnit = itemmardetail.vUnit;
                      values.vFacility = itemmardetail.vFacility;
                      values.vLocation = itemmardetail.vLocation;
                      values.vDate = itemmardetail.vDate;
                      values.vLotNo = itemmardetail.vLotNo;
                    }}
                  >
                    Add
                  </Button>
                </Grid>
              </Grid>
            ) : null}
            <MaterialTable
              id="root_mardetail"
              title={``}
              columns={columnitemdetails}
              // data={itemdetailReducer.result ? itemdetailReducer.result : []}
              data={itemdetail}
              options={{
                toolbar: true,
                search: true,
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
              }}
              actions={[
                (rowData) => ({
                  icon: AddIcon,
                  tooltip: "Add",
                  iconProps: { color: "primary" },
                  onClick: (event, rowData) => {
                    console.log(JSON.stringify(rowData));
                    let formData = new FormData();
                    formData.append("vMARNumber", marhead.vMARNumber);
                    formData.append("vPrefix", marhead.vPrefix);
                    formData.append("vTypeAdjust", itemmardetail.vTypeAdjust);
                    formData.append("vItemLine", "");
                    formData.append("vItem", rowData.MLITNO);
                    formData.append("vItemDesc", rowData.MMFUDS);
                    formData.append("vFacility", rowData.MLFACI);
                    formData.append("vWarehouse", rowData.MLWHLO);
                    formData.append("vLocation", rowData.MLWHSL);
                    formData.append("vLotNo", rowData.MLBANO);
                    formData.append("vDate", rowData.MLIDDT);
                    formData.append("vUnit", rowData.MMUNMS);
                    formData.append("vQty", rowData.ONHAND);
                    formData.append("vUnitPrice", rowData.MTTRPR);
                    formData.append("vAmount", rowData.AMT);
                    formData.append("vRemark1", "");
                    formData.append("vRemark2", "");
                    formData.append("vStatus", "00");
                    (async function() {
                      await dispatch(mardetailActions.addMARDetail(formData));

                      const dataDelete = [...itemdetail];
                      const index = rowData.tableData.id;
                      dataDelete.splice(index, 1);
                      setItemDetail([...dataDelete]);

                      // await dispatch(
                      //   itemdetialActions.fetchItemDetails(
                      //     itemmardetail.vWarehouse,
                      //     itemmardetail.vItem
                      //   )
                      // );
                    })();
                  },
                }),
              ]}
              editable={{
                onRowUpdate: (newData, oldData) =>
                  new Promise((resolve, reject) => {
                    // console.log("onRowUpdate: " + JSON.stringify(newData));
                    if (
                      newData.ONHAND > 0 &&
                      newData.ONHAND <= oldData.MLSTQT
                    ) {
                      // console.log("true");
                      const dataUpdate = [...itemdetail];
                      const index = oldData.tableData.id;
                      dataUpdate[index] = newData;
                      setItemDetail([...dataUpdate]);
                      handleCheckWHSApprove([...dataUpdate]);
                      handleCheckSubmit([...dataUpdate]);
                    }

                    resolve();
                  }),
              }}
            />
          </DialogContent>

          <DialogActions>
            <div>
              <Button onClick={handleDialogClose} color="default">
                Close
              </Button>
            </div>
          </DialogActions>
        </form>
      </Dialog>
    );
  };

  const showApprove = ({ values }, BU) => {
    // console.log("BU: " + BU);
    return (
      <div id="approve">
        {BU === "BU01"
          ? BU01({ values })
          : BU == "BU02"
          ? BU02({ values })
          : BU03({ values })}
      </div>
    );
  };

  const BU01 = ({ values }) => {
    return (
      <div id="approve">
        <Grid container item xs spacing={1}>
          <Grid item xs={6} md={2}>
            <TextField
              className={classes.margin}
              error={true}
              disabled={editdisable}
              fullWidth
              select
              size="small"
              variant="outlined"
              margin="normal"
              required
              id="vApprove1"
              label="Costing Mgr."
              value={marhead.vApprove1}
              values={(values.vApprove1 = marhead.vApprove1)}
              onChange={(event) => {
                // console.log(event.target.value);
                setMARHead({
                  ...marhead,
                  vApprove1: event.target.value,
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
          {/* <Grid item xs={6} md={2}>
            <TextField
              className={classes.margin}
              error={true}
              disabled={editdisable}
              fullWidth
              select
              size="small"
              variant="outlined"
              margin="normal"
              required
              id="vApprove2"
              label="Asst. COO"
              value={marhead.vApprove2}
              values={(values.vApprove2 = marhead.vApprove2)}
              onChange={(event) => {
                // console.log(event.target.value);
                setMARHead({
                  ...marhead,
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
          </Grid> */}
          {/* COO Name */}
          {cooapprove ? (
            <Grid item xs={6} md={2}>
              <TextField
                className={classes.margin}
                error={cooapprove}
                required={cooapprove}
                disabled={editdisable}
                fullWidth
                select
                size="small"
                variant="outlined"
                margin="normal"
                // required
                id="vApprove2"
                label="Asst. COO"
                value={marhead.vApprove2}
                values={(values.vApprove2 = marhead.vApprove2)}
                onChange={(event) => {
                  // console.log(event.target.value);
                  setMARHead({
                    ...marhead,
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
          ) : null}
          <Grid item xs={6} md={2}>
            <TextField
              className={classes.margin}
              error={true}
              disabled={editdisable}
              fullWidth
              select
              size="small"
              variant="outlined"
              margin="normal"
              required
              id="vApprove3"
              label="CFO"
              // value={marhead.vApprove3}
              value={initialStateMARHead.vApprove3}
              values={(values.vApprove3 = initialStateMARHead.vApprove3)}
              onChange={(event) => {
                // console.log(event.target.value);
                setMARHead({
                  ...marhead,
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
              error={true}
              disabled={editdisable}
              fullWidth
              select
              size="small"
              variant="outlined"
              margin="normal"
              required
              id="vApprove4"
              label="Group Acc. Director"
              // value={marhead.vApprove4}
              value={initialStateMARHead.vApprove4}
              values={(values.vApprove4 = initialStateMARHead.vApprove4)}
              onChange={(event) => {
                // console.log(event.target.value);
                setMARHead({
                  ...marhead,
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
          <Grid item xs={6} md={2}>
            <TextField
              className={classes.margin}
              error={true}
              disabled={editdisable}
              fullWidth
              select
              size="small"
              variant="outlined"
              margin="normal"
              required
              id="vApprove5"
              label="CEO"
              // value={marhead.vApprove5}
              value={initialStateMARHead.vApprove5}
              values={(values.vApprove5 = initialStateMARHead.vApprove5)}
              onChange={(event) => {
                // console.log(event.target.value);
                setMARHead({
                  ...marhead,
                  vApprove5: event.target.value,
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
              error={true}
              disabled={editdisable}
              fullWidth
              // select
              size="small"
              variant="outlined"
              margin="normal"
              required
              id="vAppICT"
              label="ICT"
              // value={marhead.vAppICT}
              value={initialStateMARHead.vAppICT}
              values={(values.vAppICT = initialStateMARHead.vAppICT)}
              onChange={(event) => {
                // console.log(event.target.value);
                setMARHead({
                  ...marhead,
                  vAppICT: event.target.value,
                });
              }}
              InputLabelProps={{ shrink: true }}
              SelectProps={{
                native: true,
              }}
            >
              {/* <option />
                {approves.map((option) => (
                  <option key={option.ID} value={option.US_LOGIN}>
                    {option.US_LOGIN}
                  </option>
                ))} */}
            </TextField>
          </Grid>

          <Grid item xs={6} md={2}>
            <TextField
              className={classes.margin}
              error={true}
              disabled={editdisable}
              fullWidth
              // select
              size="small"
              variant="outlined"
              margin="normal"
              required
              id="vAppCIO"
              label="CIO"
              // value={marhead.vAppCIO}
              value={initialStateMARHead.vAppCIO}
              values={(values.vAppCIO = initialStateMARHead.vAppCIO)}
              onChange={(event) => {
                // console.log(event.target.value);
                setMARHead({
                  ...marhead,
                  vAppCIO: event.target.value,
                });
              }}
              InputLabelProps={{ shrink: true }}
              SelectProps={{
                native: true,
              }}
            >
              {/* <option />
                {approves.map((option) => (
                  <option key={option.ID} value={option.US_LOGIN}>
                    {option.US_LOGIN}
                  </option>
                ))} */}
            </TextField>
          </Grid>
        </Grid>
      </div>
    );
  };

  const BU02 = ({ values }) => {
    return (
      <div id="approve">
        <Grid container item xs spacing={1}>
          <Grid item xs={6} md={2}>
            <TextField
              className={classes.margin}
              error={true}
              disabled={editdisable}
              fullWidth
              select
              size="small"
              variant="outlined"
              margin="normal"
              required
              id="vApprove1"
              label="Costing Mgr."
              value={marhead.vApprove1}
              values={(values.vApprove1 = marhead.vApprove1)}
              onChange={(event) => {
                // console.log(event.target.value);
                setMARHead({
                  ...marhead,
                  vApprove1: event.target.value,
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
          {/* <Grid item xs={6} md={2}>
            <TextField
              className={classes.margin}
              error={true}
              disabled={editdisable}
              fullWidth
              select
              size="small"
              variant="outlined"
              margin="normal"
              required
              id="vApprove2"
              label="Asst. COO"
              value={marhead.vApprove2}
              values={(values.vApprove2 = marhead.vApprove2)}
              onChange={(event) => {
                // console.log(event.target.value);
                setMARHead({
                  ...marhead,
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
          </Grid> */}

          {/* COO Name */}
          {cooapprove ? (
            <Grid item xs={6} md={2}>
              <TextField
                className={classes.margin}
                error={cooapprove}
                required={cooapprove}
                disabled={editdisable}
                fullWidth
                select
                size="small"
                variant="outlined"
                margin="normal"
                // required
                id="vApprove2"
                label="Asst. COO"
                value={marhead.vApprove2}
                values={(values.vApprove2 = marhead.vApprove2)}
                onChange={(event) => {
                  // console.log(event.target.value);
                  setMARHead({
                    ...marhead,
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
          ) : null}
          <Grid item xs={6} md={2}>
            <TextField
              className={classes.margin}
              error={true}
              disabled={editdisable}
              fullWidth
              select
              size="small"
              variant="outlined"
              margin="normal"
              required
              id="vApprove3"
              label="CFO"
              // value={marhead.vApprove3}
              value={initialStateMARHead.vApprove3}
              values={(values.vApprove3 = initialStateMARHead.vApprove3)}
              onChange={(event) => {
                // console.log(event.target.value);
                setMARHead({
                  ...marhead,
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
              error={true}
              disabled={editdisable}
              fullWidth
              select
              size="small"
              variant="outlined"
              margin="normal"
              required
              id="vApprove4"
              label="Group Acc. Director"
              // value={marhead.vApprove4}
              value={initialStateMARHead.vApprove4}
              values={(values.vApprove4 = initialStateMARHead.vApprove4)}
              onChange={(event) => {
                // console.log(event.target.value);
                setMARHead({
                  ...marhead,
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
          <Grid item xs={6} md={2}>
            <TextField
              className={classes.margin}
              error={true}
              disabled={editdisable}
              fullWidth
              select
              size="small"
              variant="outlined"
              margin="normal"
              // required
              id="vApprove5"
              label="CEO"
              // value={marhead.vApprove5}
              value={initialStateMARHead.vApprove5}
              values={(values.vApprove5 = initialStateMARHead.vApprove5)}
              onChange={(event) => {
                // console.log(event.target.value);
                setMARHead({
                  ...marhead,
                  vApprove5: event.target.value,
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
              error={true}
              disabled={editdisable}
              fullWidth
              // select
              size="small"
              variant="outlined"
              margin="normal"
              required
              id="vAppICT"
              label="ICT"
              // value={marhead.vAppICT}
              value={initialStateMARHead.vAppICT}
              values={(values.vAppICT = initialStateMARHead.vAppICT)}
              onChange={(event) => {
                // console.log(event.target.value);
                setMARHead({
                  ...marhead,
                  vAppICT: event.target.value,
                });
              }}
              InputLabelProps={{ shrink: true }}
              SelectProps={{
                native: true,
              }}
            >
              {/* <option />
                {approves.map((option) => (
                  <option key={option.ID} value={option.US_LOGIN}>
                    {option.US_LOGIN}
                  </option>
                ))} */}
            </TextField>
          </Grid>

          <Grid item xs={6} md={2}>
            <TextField
              className={classes.margin}
              error={true}
              disabled={editdisable}
              fullWidth
              // select
              size="small"
              variant="outlined"
              margin="normal"
              required
              id="vAppCIO"
              label="CIO"
              // value={marhead.vAppCIO}
              value={initialStateMARHead.vAppCIO}
              values={(values.vAppCIO = initialStateMARHead.vAppCIO)}
              onChange={(event) => {
                // console.log(event.target.value);
                setMARHead({
                  ...marhead,
                  vAppCIO: event.target.value,
                });
              }}
              InputLabelProps={{ shrink: true }}
              SelectProps={{
                native: true,
              }}
            >
              {/* <option />
                {approves.map((option) => (
                  <option key={option.ID} value={option.US_LOGIN}>
                    {option.US_LOGIN}
                  </option>
                ))} */}
            </TextField>
          </Grid>
        </Grid>
      </div>
    );
  };

  const BU03 = ({ values }) => {
    return (
      <div id="approve">
        <Grid container item xs spacing={1}>
          <Grid item xs={6} md={2}>
            <TextField
              className={classes.margin}
              error={true}
              disabled={editdisable}
              fullWidth
              select
              size="small"
              variant="outlined"
              margin="normal"
              required
              id="vApprove1"
              label="Costing Mgr."
              value={marhead.vApprove1}
              values={(values.vApprove1 = marhead.vApprove1)}
              onChange={(event) => {
                // console.log(event.target.value);
                setMARHead({
                  ...marhead,
                  vApprove1: event.target.value,
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
              error={true}
              disabled={editdisable}
              fullWidth
              select
              size="small"
              variant="outlined"
              margin="normal"
              required
              id="vApprove3"
              label="CFO"
              // value={marhead.vApprove3}
              value={initialStateMARHead.vApprove3}
              values={(values.vApprove3 = initialStateMARHead.vApprove3)}
              onChange={(event) => {
                // console.log(event.target.value);
                setMARHead({
                  ...marhead,
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
              error={true}
              disabled={editdisable}
              fullWidth
              select
              size="small"
              variant="outlined"
              margin="normal"
              required
              id="vApprove4"
              label="Group Acc. Director"
              // value={marhead.vApprove4}
              value={initialStateMARHead.vApprove4}
              values={(values.vApprove4 = initialStateMARHead.vApprove4)}
              onChange={(event) => {
                // console.log(event.target.value);
                setMARHead({
                  ...marhead,
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
          <Grid item xs={6} md={2}>
            <TextField
              className={classes.margin}
              error={true}
              disabled={editdisable}
              fullWidth
              select
              size="small"
              variant="outlined"
              margin="normal"
              required
              id="vApprove5"
              label="CEO"
              // value={marhead.vApprove5}
              value={initialStateMARHead.vApprove5}
              values={(values.vApprove5 = initialStateMARHead.vApprove5)}
              onChange={(event) => {
                // console.log(event.target.value);
                setMARHead({
                  ...marhead,
                  vApprove5: event.target.value,
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
              error={true}
              disabled={editdisable}
              fullWidth
              // select
              size="small"
              variant="outlined"
              margin="normal"
              required
              id="vAppICT"
              label="ICT"
              // value={marhead.vAppICT}
              value={initialStateMARHead.vAppICT}
              values={(values.vAppICT = initialStateMARHead.vAppICT)}
              onChange={(event) => {
                // console.log(event.target.value);
                setMARHead({
                  ...marhead,
                  vAppICT: event.target.value,
                });
              }}
              InputLabelProps={{ shrink: true }}
              SelectProps={{
                native: true,
              }}
            >
              {/* <option />
                {approves.map((option) => (
                  <option key={option.ID} value={option.US_LOGIN}>
                    {option.US_LOGIN}
                  </option>
                ))} */}
            </TextField>
          </Grid>

          <Grid item xs={6} md={2}>
            <TextField
              className={classes.margin}
              error={true}
              disabled={editdisable}
              fullWidth
              // select
              size="small"
              variant="outlined"
              margin="normal"
              required
              id="vAppCIO"
              label="CIO"
              // value={marhead.vAppCIO}
              value={initialStateMARHead.vAppCIO}
              values={(values.vAppCIO = initialStateMARHead.vAppCIO)}
              onChange={(event) => {
                // console.log(event.target.value);
                setMARHead({
                  ...marhead,
                  vAppCIO: event.target.value,
                });
              }}
              InputLabelProps={{ shrink: true }}
              SelectProps={{
                native: true,
              }}
            >
              {/* <option />
                {approves.map((option) => (
                  <option key={option.ID} value={option.US_LOGIN}>
                    {option.US_LOGIN}
                  </option>
                ))} */}
            </TextField>
          </Grid>
        </Grid>
      </div>
    );
  };

  const showFile = () => {
    return (
      <div id="file">
        <MaterialTable
          id="root_marfile"
          title={`Attach file : `}
          columns={columnfiles}
          data={marfile}
          // isLoading={orderheadReducer.result ? false : true}
          options={{
            search: false,
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
          }}
          components={{
            Toolbar: (props) => (
              <div style={{ padding: "0px 10px" }}>
                <Grid container>
                  <h4>Attach file :</h4>
                  <Button
                    size="large"
                    color="primary"
                    disabled={editdisable}
                    startIcon={<AttachFileIcon />}
                    onClick={(event, rowData) => {
                      setSelectedProduct("rowData");
                      setOpenDialogFile(true);
                    }}
                  />
                </Grid>
              </div>
            ),
          }}
          editable={{
            onRowDelete: (oldData) =>
              new Promise((resolve, reject) => {
                console.log("onRowDelete: " + JSON.stringify(oldData));
                let formData = new FormData();
                formData.append("vMARNumber", marnumber.vMARSelectNumber);
                formData.append("vPrefix", marhead.vPrefix);
                formData.append("vFileLine", oldData.MFLINE);
                // formData.append("vFile", "");
                formData.append("vFileName", oldData.MFNAME);
                // formData.append("vFlieType", "");
                // formData.append("vFilePath", "");
                // formData.append("vRemark1", "");
                // formData.append("vRemark2", "");

                if (marhead.vPrefix === oldData.MFPREF) {
                  (async function() {
                    await dispatch(
                      marfileActions.deleteMARfile(formData, props.history)
                    );

                    const dataDelete = [...marfile];
                    const index = oldData.tableData.id;
                    dataDelete.splice(index, 1);
                    setMARFile([...dataDelete]);
                  })();
                }

                resolve();
              }),
          }}
          actions={[
            (rowData) => ({
              icon: GetAppIcon,
              tooltip: "Download",
              iconProps: { color: "primary" },
              onClick: (event, rowData) => {
                // console.log("rowData: " + JSON.stringify([rowData]));
                window.open(
                  `http://localhost:8080/mar_api/data/file/${rowData.MFPREF +
                    "-" +
                    rowData.MFORNO}/${rowData.MFLINE}/${
                    rowData.MFNAME
                  }/${loginActions.getToken()}`,
                  "_blank"
                );
              },
            }),
          ]}
        />
      </div>
    );
  };

  const showDialogFile = ({
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
        open={openDialogFile}
        keepMounted
        onClose={() => {}}
        aria-labelledby="alert-dialog-slide-title"
        aria-describedby="alert-dialog-slide-description"
      >
        <form onSubmit={handleSubmit}>
          <DialogTitle id="alert-dialog-slide-title"></DialogTitle>
          <DialogContent>
            <TextField
              fullWidth
              required={imagereqdisable}
              inputRef={inputRef}
              id="vFile"
              label="File: jpeg, pdf, xlsx, docx"
              type="file"
              values={(values.vImageFile = itemmardetail.vImageFile)}
              InputLabelProps={{ shrink: true }}
              inputProps={{
                accept:
                  "image/png, image/gif, image/jpeg, .pdf, .xls, .xlsx, .doc, .docx",
              }}
              onChange={(event) => {
                // console.log(event.target.files);
                // console.log(event.target.files[0].name);
                if (event.target.files[0]) {
                  const file = event.target.files[0];
                  const fileName = event.target.files[0].name;
                  const reader = new FileReader();
                  reader.readAsDataURL(file);
                  reader.onload = () => {
                    setFileMARDetail({
                      ...filemardetail,
                      vFile: file,
                      vFileName: fileName,
                      vFilePath: marhead.vMARNumber + "_" + fileName,
                    });

                    values.vFile = file;
                    values.vFileName = fileName;
                    values.vFilePath = marhead.vMARNumber + "_" + fileName;
                  };
                }
              }}
            />
          </DialogContent>

          <DialogActions>
            <div>
              <Button onClick={handleDialogFileClose} color="default">
                Close
              </Button>
              <Button
                id="vSaveItem"
                type="submit"
                color="primary"
                onClick={(event) => {
                  if (itemmardetail.vItemLine === "") {
                    values.vSubmit = "create";
                    values.vStatus = "00";
                  } else {
                    values.vSubmit = "update";
                    values.vStatus = "00";
                  }
                }}
              >
                {itemmardetail.vItemLine === "" ? "Save" : "Update"}
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

  const datafiles = [
    {
      ID: 1,
      MANAME: "TEST.pdf",
    },
    {
      ID: 2,
      MANAME: "_แก้ไข Catch weight.xlsx",
    },
  ];

  const columnfiles = [
    {
      title: "Line",
      field: "MFLINE",
      type: "numeric",
      editable: "never",
      width: "10%",
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
      },
      render: (item) => (
        <Typography variant="body1" noWrap>
          {item.MFLINE}
        </Typography>
      ),
    },
    {
      title: "Description",
      field: "MFNAME",
      editable: "never",
      width: "80%",
      headerStyle: { whiteSpace: "nowrap", textAlign: "center" },
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
          {item.MFNAME}
        </Typography>
      ),
    },
    {
      title: "Upload",
      field: "MFENUS",
      editable: "never",
      width: "10%",
      headerStyle: { whiteSpace: "nowrap", textAlign: "center" },
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
          {item.MFENUS}
        </Typography>
      ),
    },
  ];

  const columndetails = [
    {
      title: "Remark",
      field: "MLREM1",
      editable: "never",
      headerStyle: { maxWidth: 200, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.MLREM1}
        </Typography>
      ),
    },
    {
      title: "Order No. (M3)",
      field: "MLRENO",
      // type: "numeric",
      editable: "never",
      width: 50,
      headerStyle: {
        maxWidth: 50,
        whiteSpace: "nowrap",
        textAlign: "center",
      },
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
          {item.MLRENO}
        </Typography>
      ),
    },
    {
      title: "Type",
      field: "MLTYPE",
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
          {item.MLTYPE === "0" ? "-" : "+"}
        </Typography>
      ),
    },
    {
      title: "No.",
      field: "MLLINE",
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
          {item.MLLINE}
        </Typography>
      ),
    },
    {
      title: "Item Code",
      field: "MLITNO",
      // type: "numeric",
      editable: "never",
      width: 50,
      headerStyle: {
        maxWidth: 50,
        whiteSpace: "nowrap",
        textAlign: "center",
      },
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
          {item.MLITNO}
        </Typography>
      ),
    },
    {
      title: "Name",
      field: "MLITDE",
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
          {item.MLITDE}
        </Typography>
      ),
    },
    {
      title: "Fac.",
      field: "MLFACI",
      editable: "never",
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
          {item.MLFACI}
        </Typography>
      ),
    },
    {
      title: "Whs.",
      field: "MLWHLO",
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
          {item.MLWHLO}
        </Typography>
      ),
    },
    {
      title: "Location",
      field: "MLLOCA",
      editable: "never",
      headerStyle: { maxWidth: 50, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.MLLOCA}
        </Typography>
      ),
    },
    {
      title: "Lot No.",
      field: "MLLOTN",
      // editable: "never",
      headerStyle: { maxWidth: 50, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.MLLOTN}
        </Typography>
      ),
    },
    {
      title: "Date",
      field: "MLDATE",
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
          {moment(item.MLDATE).format("DD/MM/YYYY")}
        </Typography>
      ),
    },
    {
      title: "Unit",
      field: "MLUNIT",
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
          {item.MLUNIT}
        </Typography>
      ),
    },
    {
      title: "Qty.",
      field: "MLQTY",
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
            value={item.MLQTY}
            displayType={"text"}
            thousandSeparator={true}
            // prefix={"$"}
          />
        </Typography>
      ),
    },
    {
      title: "Unit Price",
      field: "MLPRIC",
      // editable: "never",
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
          <NumberFormat
            value={item.MLPRIC}
            displayType={"text"}
            thousandSeparator={true}
            // prefix={"$"}
          />
        </Typography>
      ),
    },
    {
      title: "Amount",
      field: "AMT",
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
            // value={item.AMT}
            value={(item.MLQTY * item.MLPRIC).toFixed(4)}
            displayType={"text"}
            thousandSeparator={true}
            // prefix={"$"}
          />
        </Typography>
      ),
    },
  ];

  const columnitemdetails = [
    {
      title: "No.",
      field: "ID",
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
          {item.ID}
        </Typography>
      ),
    },
    {
      title: "Item Code",
      field: "MLITNO",
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
          {item.MLITNO}
        </Typography>
      ),
    },
    {
      title: "Name",
      field: "MMFUDS",
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
          {item.MMFUDS}
        </Typography>
      ),
    },
    {
      title: "Fac.",
      field: "MLFACI",
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
          {item.MLFACI}
        </Typography>
      ),
    },
    {
      title: "Whs.",
      field: "MLWHLO",
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
          {item.MLWHLO}
        </Typography>
      ),
    },
    {
      title: "Location",
      field: "MLWHSL",
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
          {item.MLWHSL}
        </Typography>
      ),
    },
    {
      title: "Lot No.",
      field: "MLBANO",
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
          {item.MLBANO}
        </Typography>
      ),
    },
    {
      title: "Date",
      field: "MLIDDT",
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
          {moment(item.MLIDDT).format("DD/MM/YYYY")}
        </Typography>
      ),
    },
    {
      title: "Unit",
      field: "MMUNMS",
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
          {item.MMUNMS}
        </Typography>
      ),
    },
    {
      title: "Qty.",
      field: "ONHAND",
      // editable: "never",
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
            value={item.ONHAND}
            displayType={"text"}
            thousandSeparator={true}
            // prefix={"$"}
          />
        </Typography>
      ),
    },
    {
      title: "Unit Price",
      field: "MTTRPR",
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
          <NumberFormat
            value={item.MTTRPR}
            displayType={"text"}
            thousandSeparator={true}
            // prefix={"$"}
          />
        </Typography>
      ),
    },
    {
      title: "Amount",
      field: "AMT",
      editable: "never",
      // type: "numeric",
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
          <NumberFormat
            value={item.AMT}
            displayType={"text"}
            thousandSeparator={true}
            // prefix={"$"}
          />
        </Typography>
      ),
    },
    // {
    //   title: "Remark",
    //   field: "ALREM1",
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
    //       {item.ALREM1}
    //     </Typography>
    //   ),
    // },
  ];

  return (
    <div className={classes.root}>
      {/* MAR Head */}
      {/* <p>#Debug MARNumber {JSON.stringify(marnumber)}</p> */}
      {/* <p>#Debug MARHead {JSON.stringify(marhead)}</p> */}
      <Formik
        initialValues={{
          vMARNumber: "",
          vDate: "",
          vPostDate: "",
          vMonth: "",
          vType: "",
          vPrefix: "",
          vBU: "",
          vCostcenter: "",
          vAccountant: "",
          vRequestor: "",
          vRemark: "",
          vPurpose: "",
          vApprove1: "",
          vApprove2: "",
          vApprove3: "",
          vApprove4: "",
          vApprove5: "",
          vAppWHS: "",
          vAppICT: "",
          vAppCIO: "",
          vAccRemark: "",
          vAppRemark1: "",
          vAppRemark2: "",
          vAppRemark3: "",
          vAppRemark4: "",
          vAppRemark5: "",
          vWHSRemark: "",
          vICTRemark: "",
          vCIORemark: "",
          vReason: "",
          vStatus: "",
          vFromStatus: "",
          vToStatus: "",
          vSubmit: "",
        }}
        onSubmit={(values, { setSubmitting }) => {
          // Set values before append data.
          values.vPrefix = marhead.vPrefix;
          values.vMARNumber = marnumber.vMARSelectNumber;
          values.vReason = marhead.vReason;
          // alert(JSON.stringify(values));

          let formData = new FormData();
          formData.append("vMARNumber", values.vMARNumber);
          formData.append("vDate", values.vDate);
          formData.append("vPostDate", values.vPostDate);
          formData.append("vMonth", values.vMonth);
          formData.append("vType", values.vType);
          formData.append("vPrefix", values.vPrefix);
          formData.append("vBU", values.vBU);
          formData.append("vCostcenter", values.vCostcenter);
          formData.append("vAccountant", values.vAccountant);
          formData.append("vRequestor", values.vRequestor);
          formData.append("vRemark", values.vRemark);
          formData.append("vPurpose", values.vPurpose);
          formData.append("vApprove1", values.vApprove1);
          formData.append("vApprove2", values.vApprove2);
          formData.append("vApprove3", values.vApprove3);
          formData.append("vApprove4", values.vApprove4);
          formData.append("vApprove5", values.vApprove5);
          formData.append("vAppWHS", values.vAppWHS);
          formData.append("vAppICT", values.vAppICT);
          formData.append("vAppCIO", values.vAppCIO);
          formData.append("vAccRemark", values.vAccRemark);
          formData.append("vAppRemark1", "");
          formData.append("vAppRemark2", "");
          formData.append("vAppRemark3", "");
          formData.append("vAppRemark4", "");
          formData.append("vAppRemark5", "");
          formData.append("vWHSRemark", "");
          formData.append("vICTRemark", "");
          formData.append("vCIORemark", "");
          formData.append("vReason", values.vReason);
          formData.append("vStatus", values.vStatus);
          formData.append("vFromStatus", values.vFromStatus);
          formData.append("vToStatus", values.vToStatus);
          formData.append("vSubmit", values.vSubmit);

          if (values.vSubmit === "create") {
            (async function() {
              let data = await dispatch(marheadActions.addMARHeadV2(formData));
              alert(JSON.stringify(data));

              let fromStatus = "00";
              let toStatus = "05";
              await dispatch(
                marnumberActions.getMARNumber(
                  values.vPrefix,
                  fromStatus,
                  toStatus
                )
              );
              setMARNumber({ ...marnumber, vMARSelectNumber: data.message });
              handleSearch(data.message);
            })();
          }

          if (values.vSubmit === "update") {
            (async function() {
              let data = await dispatch(marheadActions.updateMARHead(formData));
              alert(JSON.stringify(data));
              // setMARNumber({ ...marnumber, vADRSelectNumber: data.message });
              // handleSearch(data.message);
            })();
          }

          if (values.vSubmit === "approve") {
            setSubmitDisable(true);
            setCancelDisable(true);
            setAllocateDisable(true);
            setSuccess(false);
            setLoading(true);

            (async function() {
              let data = await dispatch(marheadActions.approveMAR(formData));
              alert(JSON.stringify(data));
              let fromStatus = "50";
              let toStatus = "50";
              await dispatch(
                marnumberActions.getMARNumber(
                  values.vPrefix,
                  fromStatus,
                  toStatus
                )
              );
              handleClear();
              setSuccess(true);
              setLoading(false);
            })();
          }

          if (values.vSubmit === "allocate") {
            setSubmitDisable(true);
            setCancelDisable(true);
            setAllocateDisable(true);
            setSuccess(false);
            setLoadingAllocate(true);

            (async function() {
              let data = await dispatch(
                marheadActions.allocateMAR(values.vMARNumber)
              );
              alert(JSON.stringify(data));

              setSuccess(true);
              setLoading(false);
            })();

            setSubmitDisable(false);
            setCancelDisable(false);
            setAllocateDisable(false);
            setSuccess(true);
            setLoadingAllocate(false);
          }

          if (values.vSubmit === "reject") {
            if (window.confirm(`Reject order ${marhead.vMARNumber}?`)) {
              setSubmitDisable(true);
              setCancelDisable(true);
              setAllocateDisable(true);
              setSuccess(false);
              setLoadingCancel(true);

              (async function() {
                let data = await dispatch(marheadActions.rejectMAR(formData));
                alert(JSON.stringify(data));

                let fromStatus = "00";
                let toStatus = "05";
                await dispatch(
                  marnumberActions.getMARNumber(
                    values.vPrefix,
                    fromStatus,
                    toStatus
                  )
                );
                handleClear();

                setSuccess(true);
                setLoadingCancel(false);
              })();
            }
          }
        }}
      >
        {(props) => showForm(props)}
      </Formik>

      {/* DialogFile */}
      <Formik
        initialValues={{
          vMARNumber: "",
          vPrefix: "",
          vFileLine: "",
          vFile: "",
          vFileName: "",
          vFlieType: "",
          vFilePath: "",
        }}
        onSubmit={(values, { setSubmitting }) => {
          // alert(JSON.stringify(values));
          let formData = new FormData();
          formData.append("vMARNumber", marnumber.vMARSelectNumber);
          formData.append("vPrefix", marhead.vPrefix);
          formData.append("vFileLine", "");
          formData.append("vFile", values.vFile);
          formData.append("vFileName", values.vFileName);
          formData.append("vFlieType", values.vFlieType);
          formData.append("vFilePath", values.vFilePath);
          formData.append("vRemark1", "");
          formData.append("vRemark2", "");

          (async function() {
            await dispatch(marfileActions.addMARfile(formData, props.history));
            await dispatch(
              marfileActions.fetchMARFile(marnumber.vMARSelectNumber)
            );
            handleDialogFileClose();
          })();
        }}
      >
        {(props) => showDialogFile(props)}
      </Formik>
    </div>
  );
};
