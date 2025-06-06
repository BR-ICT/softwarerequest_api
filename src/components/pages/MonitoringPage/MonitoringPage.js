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
import RefreshIcon from "@material-ui/icons/Refresh";
import MailOutlineIcon from "@material-ui/icons/MailOutline";
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
import * as sendemailActions from "./../../../actions/sendemail.action";

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

  const initialStateMARHead = {
    vMARNumber: "",
    vDate: moment(new Date()).format("YYYY-MM-DD"),
    vPostDate: moment(new Date()).format("YYYY-MM-DD"),
    vFromDate: "", //moment(new Date()).format("YYYY-MM-DD"),
    vToDate: "", //moment(new Date()).format("YYYY-MM-DD"),
    vMonth: moment(new Date()).format("YYYYMM"),
    vType: "",
    vPrefix: loginActions.getTokenRoleACC() ? "A" : "D",
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

  const [prefixdisable, setPrefixDisable] = useState(false);
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
  const [showstatus, setShowStatus] = useState(true);

  useEffect(() => {
    marheadReducer.result = null;
    mardetailReducer.result = null;
    marfileReducer.result = null;
    (async function() {
      let fromStatus = "00";
      let toStatus = "99";
      await dispatch(
        marnumberActions.getMARNumberMonitoring(
          marhead.vPrefix,
          fromStatus,
          toStatus
        )
      );
    })();

    if (loginActions.getTokenRoleACC()) {
      setPrefixDisable(false);
    } else {
      setPrefixDisable(true);
    }
  }, []);

  useEffect(() => {
    const mardetails = mardetailReducer.result ? mardetailReducer.result : [];
    setMARDetail(mardetails);
    handleCheckWHSApprove(mardetails);
    handleCheckSubmit(mardetails);
  }, [mardetailReducer]);

  const marnumbers = useMemo(() =>
    marnumberReducer.result ? marnumberReducer.result : []
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
      // setEditDisable(false);
      setAddItemDisable(false);

      let fromStatus = "00";
      let toStatus = "10";
      dispatch(marheadActions.getMARHead(marnumber, fromStatus, toStatus));
      dispatch(mardetailActions.getMARDetail(marnumber, fromStatus, toStatus));
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

  const handlePrefix = (prefix) => {
    marheadReducer.result = null;
    mardetailReducer.result = null;
    let fromStatus = "00";
    let toStatus = "99";
    dispatch(
      marnumberActions.getMARNumberMonitoring(prefix, fromStatus, toStatus)
    );
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
        // if (item.MLTYPE === "0") {
        if (item.MLRENO === "") {
          count += 1;
        } else {
          if (
            item.MGTRSL.substring(0, 1) !== "9" ||
            item.MGTRSL.substring(0, 1) !== "9"
          ) {
            count += 1;
          }
        }
        // }
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

  const prefixs = [
    {
      ID: "0",
      VALUE: "D",
    },
    {
      ID: "1",
      VALUE: "A",
    },
  ];

  const statuses = [
    {
      ID: "0",
      VALUE: "00",
    },
    {
      ID: "1",
      VALUE: "05",
    },
    {
      ID: "2",
      VALUE: "10",
    },
    {
      ID: "3",
      VALUE: "20",
    },
    {
      ID: "4",
      VALUE: "30",
    },
    {
      ID: "5",
      VALUE: "40",
    },
    {
      ID: "6",
      VALUE: "50",
    },
    {
      ID: "7",
      VALUE: "60",
    },
    {
      ID: "8",
      VALUE: "70",
    },
    {
      ID: "9",
      VALUE: "80",
    },
    {
      ID: "10",
      VALUE: "90",
    },
    {
      ID: "11",
      VALUE: "99",
    },
  ];

  const types = [
    {
      ID: "0",
      TYPE: "Adjust Stock",
    },
    {
      ID: "1",
      TYPE: "Cancel GRN",
    },
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

  const showStatus = () => {
    return (
      <form id="status">
        <Grid container style={{ marginTop: 1 }} spacing={3}>
          <Grid item xs={6} md={3}>
            {/* {`${"Status 00 = Create Order, 10 = Confirm Order, 92 = Gen CO, 95 = Print CO, 97 = Gen Inv, 99 = Cancel Order"}`} */}
            <Grid container alignItems="flex-start">
              <Typography variant="button">{`${"MAR-D Status :"}`}</Typography>
            </Grid>
            <Grid container alignItems="flex-start">
              <Typography variant="caption">{`${"00 = Create MAR"}`}</Typography>
            </Grid>
            <Grid container alignItems="flex-start">
              <Typography variant="caption">{`${"05 = Reject MAR"}`}</Typography>
            </Grid>
            <Grid container alignItems="flex-start">
              <Typography variant="caption">{`${"10 = Wait for Dept. Head approve"}`}</Typography>
            </Grid>
            <Grid container alignItems="flex-start">
              <Typography variant="caption">{`${"20 = Wait for Techical Mgr. / Asst. COO approve"}`}</Typography>
            </Grid>
            <Grid container alignItems="flex-start">
              <Typography variant="caption">{`${"40 = Wait for BU Head approve"}`}</Typography>
            </Grid>
            <Grid container alignItems="flex-start">
              <Typography variant="caption">{`${"50 = Wait for Asst. CEO approve"}`}</Typography>
            </Grid>
            <Grid container alignItems="flex-start">
              <Typography variant="caption">{`${"60 = Wait for Warehouse. Mgr. confirm"}`}</Typography>
            </Grid>
            <Grid container alignItems="flex-start">
              <Typography variant="caption">{`${"90 = Complete"}`}</Typography>
            </Grid>
            <Grid container alignItems="flex-start">
              <Typography variant="caption">{`${"99 = Cancel MAR"}`}</Typography>
            </Grid>
          </Grid>
          <Grid item xs={6} md={3}>
            {/* {`${"Status 00 = Create Order, 10 = Confirm Order, 92 = Gen CO, 95 = Print CO, 97 = Gen Inv, 99 = Cancel Order"}`} */}
            <Grid container alignItems="flex-start">
              <Typography variant="button">{`${"MAR-A Status :"}`}</Typography>
            </Grid>
            <Grid container alignItems="flex-start">
              <Typography variant="caption">{`${"00 = Create MAR"}`}</Typography>
            </Grid>
            <Grid container alignItems="flex-start">
              <Typography variant="caption">{`${"05 = Reject MAR"}`}</Typography>
            </Grid>
            <Grid container alignItems="flex-start">
              <Typography variant="caption">{`${"10 = Wait for Costing Mgr. approve"}`}</Typography>
            </Grid>
            <Grid container alignItems="flex-start">
              <Typography variant="caption">{`${"20 = Wait for Asst. COO approve"}`}</Typography>
            </Grid>
            <Grid container alignItems="flex-start">
              <Typography variant="caption">{`${"30 = Wait for CFO approve"}`}</Typography>
            </Grid>
            <Grid container alignItems="flex-start">
              <Typography variant="caption">{`${"40 = Wait for Group Accounting approve"}`}</Typography>
            </Grid>
            <Grid container alignItems="flex-start">
              <Typography variant="caption">{`${"50 = Wait for print and CEO approve"}`}</Typography>
            </Grid>
            <Grid container alignItems="flex-start">
              <Typography variant="caption">{`${"70 = Wait for ICT confirm"}`}</Typography>
            </Grid>
            <Grid container alignItems="flex-start">
              <Typography variant="caption">{`${"80 = Wait for CIO approve"}`}</Typography>
            </Grid>
            <Grid container alignItems="flex-start">
              <Typography variant="caption">{`${"90 = Complete"}`}</Typography>
            </Grid>
            <Grid container alignItems="flex-start">
              <Typography variant="caption">{`${"99 = Cancel MAR"}`}</Typography>
            </Grid>
          </Grid>
        </Grid>
      </form>
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
        <Grid container style={{ marginBottom: 2 }} spacing={3}>
          <Grid item xs={12}>
            <Paper className={classes.paper}>
              <Grid container item xs={12} spacing={1}>
                <Grid item xs={6} md={1}>
                  <TextField
                    disabled={prefixdisable}
                    fullWidth
                    select
                    size="small"
                    variant="outlined"
                    id="vPrefix"
                    label="Prefix"
                    value={marhead.vPrefix}
                    values={(values.vPrefix = marhead.vPrefix)}
                    onChange={(event) => {
                      // console.log(event.target.value);
                      setMARHead({
                        ...marhead,
                        vPrefix: event.target.value,
                      });

                      handlePrefix(event.target.value);
                    }}
                    InputLabelProps={{ shrink: true }}
                    SelectProps={{
                      native: true,
                    }}
                  >
                    {prefixs.map((option) => (
                      <option key={option.ID} value={option.VALUE}>
                        {option.VALUE}
                      </option>
                    ))}
                  </TextField>
                </Grid>

                <Grid item xs={12} md={3}>
                  <TextField
                    fullWidth
                    select
                    size="small"
                    variant="outlined"
                    id="vSelectMARNumber"
                    label="MAR Number"
                    // disabled={searchdisable}
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
                <Grid item xs={6} md={"auto"}>
                  <TextField
                    // disabled={true}
                    fullWidth
                    type="date"
                    size="small"
                    id="vFromDate"
                    label="From Date"
                    variant="outlined"
                    defaultValue={marhead.vFromDate}
                    value={marhead.vFromDate}
                    values={(values.vFromDate = marhead.vFromDate)}
                    onChange={(event) => {
                      setMARHead({
                        ...marhead,
                        vFromDate: event.target.value,
                      });
                    }}
                    InputLabelProps={{ shrink: true }}
                  />
                </Grid>
                <Grid item xs={6} md={"auto"}>
                  <TextField
                    // disabled={true}
                    fullWidth
                    type="date"
                    size="small"
                    id="vToDate"
                    label="To Date"
                    variant="outlined"
                    defaultValue={marhead.vToDate}
                    value={marhead.vToDate}
                    values={(values.vToDate = marhead.vToDate)}
                    onChange={(event) => {
                      setMARHead({
                        ...marhead,
                        vToDate: event.target.value,
                      });
                    }}
                    InputLabelProps={{ shrink: true }}
                  />
                </Grid>

                <Grid item xs={6} md={1}>
                  <TextField
                    // disabled={create ? editdisable : true}
                    fullWidth
                    select
                    size="small"
                    variant="outlined"
                    id="vStatus"
                    label="Status"
                    value={marhead.vStatus}
                    values={(values.vStatus = marhead.vStatus)}
                    onChange={(event) => {
                      // console.log(event.target.value);
                      setMARHead({
                        ...marhead,
                        vStatus: event.target.value,
                      });
                    }}
                    InputLabelProps={{ shrink: true }}
                    SelectProps={{
                      native: true,
                    }}
                  >
                    <option />
                    {statuses.map((option) => (
                      <option key={option.ID} value={option.VALUE}>
                        {option.VALUE}
                      </option>
                    ))}
                  </TextField>
                </Grid>
                <Grid item xs={6} md={"auto"}>
                  <Button
                    fullWidth
                    size="medium"
                    id="vSearch"
                    type="submit"
                    variant="contained"
                    color="primary"
                    disabled={searchdisable}
                    startIcon={<SearchIcon />}
                    onClick={(event) => {
                      values.vSubmit = "search";
                      // handleSearch(marnumber.vMARSelectNumber);
                    }}
                  >
                    Search
                  </Button>
                </Grid>
                <Grid item xs={6} md={"auto"}>
                  <ColorButtonDeepBlueGray
                    fullWidth
                    size="medium"
                    id="vClear"
                    variant="contained"
                    color="secondary"
                    startIcon={<CancelIcon />}
                    // disabled={cleardisable}
                    onClick={handleClear}
                  >
                    Clear
                  </ColorButtonDeepBlueGray>
                </Grid>
              </Grid>
              {showstatus ? showStatus() : null}
              <Grid container item xs={12} spacing={1} justify="center">
                <Button
                  size="large"
                  startIcon={
                    showstatus ? (
                      <KeyboardArrowUpIcon />
                    ) : (
                      <KeyboardArrowDownIcon />
                    )
                  }
                  onClick={() => {
                    showstatus ? setShowStatus(false) : setShowStatus(true);
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

  const columnheads = [
    {
      title: "Status",
      field: "MHSTAT",
      editable: "never",
      headerStyle: { maxWidth: 30, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.MHSTAT}
        </Typography>
      ),
    },
    {
      title: "MAR. No",
      field: "MARNUMBER",
      editable: "never",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.MARNUMBER}
        </Typography>
      ),
    },
    {
      title: "Request. dt.",
      field: "MHREDA",
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
          {moment(item.MHREDA).format("DD/MM/YYYY")}
        </Typography>
      ),
    },
    {
      title: "Post dt.",
      field: "MHPODA",
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
          {moment(item.MHPODA).format("DD/MM/YYYY")}
        </Typography>
      ),
    },
    {
      title: "Type",
      field: "MHTYPE",
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
          {item.MHTYPE}
        </Typography>
      ),
    },
    {
      title: "BU",
      field: "MHBU",
      editable: "never",
      headerStyle: { maxWidth: 30, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.MHBU}
        </Typography>
      ),
    },
    {
      title: "Cost Cen.",
      field: "MHCOCE",
      editable: "never",
      headerStyle: { maxWidth: 30, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.MHCOCE}
        </Typography>
      ),
    },
    {
      title: "Requester",
      field: "MHREQU",
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
          {item.MHREQU}
        </Typography>
      ),
    },
    {
      title: "Accountant",
      field: "MHACCT",
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
          {item.MHACCT}
        </Typography>
      ),
    },
    {
      title: "Acc. remark",
      field: "MHACCRE",
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
          {item.MHACCRE}
        </Typography>
      ),
    },
    {
      title: "Request. dt.",
      field: "MHREDA",
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
          {moment(item.MHREDA).format("DD/MM/YYYY")}
        </Typography>
      ),
    },
    {
      title: "App1 Name",
      field: "MHAPP1",
      editable: "never",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.MHAPP1}
        </Typography>
      ),
    },
    {
      title: "App1 Date",
      field: "MHAPDA1",
      editable: "never",
      type: "date",
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
          {item.MHAPDA1 ? moment(item.MHAPDA1).format("DD/MM/YYYY") : ""}
        </Typography>
      ),
    },
    {
      title: "App1 Remark",
      field: "MHAPRE1",
      editable: "never",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.MHAPRE1}
        </Typography>
      ),
    },

    {
      title: "App2 Name",
      field: "MHAPP2",
      editable: "never",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.MHAPP2}
        </Typography>
      ),
    },
    {
      title: "App2 Date",
      field: "MHAPDA2",
      editable: "never",
      type: "date",
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
          {item.MHAPDA2 ? moment(item.MHAPDA2).format("DD/MM/YYYY") : ""}
        </Typography>
      ),
    },
    {
      title: "App2 Remark",
      field: "MHAPRE2",
      editable: "never",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.MHAPRE2}
        </Typography>
      ),
    },

    {
      title: "App3 Name",
      field: "MHAPP3",
      editable: "never",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.MHAPP3}
        </Typography>
      ),
    },
    {
      title: "App3 Date",
      field: "MHAPDA3",
      editable: "never",
      type: "date",
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
          {item.MHAPDA3 ? moment(item.MHAPDA3).format("DD/MM/YYYY") : ""}
        </Typography>
      ),
    },
    {
      title: "App3 Remark",
      field: "MHAPRE3",
      editable: "never",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.MHAPRE3}
        </Typography>
      ),
    },

    {
      title: "App4 Name",
      field: "MHAPP4",
      editable: "never",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.MHAPP4}
        </Typography>
      ),
    },
    {
      title: "App4 Date",
      field: "MHAPDA4",
      editable: "never",
      type: "date",
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
          {item.MHAPDA4 ? moment(item.MHAPDA4).format("DD/MM/YYYY") : ""}
        </Typography>
      ),
    },
    {
      title: "App4 Remark",
      field: "MHAPRE4",
      editable: "never",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.MHAPRE4}
        </Typography>
      ),
    },

    {
      title: "App5 Name",
      field: "MHAPP5",
      editable: "never",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.MHAPP5}
        </Typography>
      ),
    },
    {
      title: "App5 Date",
      field: "MHAPDA5",
      editable: "never",
      type: "date",
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
          {item.MHAPDA5 ? moment(item.MHAPDA5).format("DD/MM/YYYY") : ""}
        </Typography>
      ),
    },
    {
      title: "App5 Remark",
      field: "MHAPRE5",
      editable: "never",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.MHAPRE5}
        </Typography>
      ),
    },

    {
      title: "WHS. Name",
      field: "MHAPWHS",
      editable: "never",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.MHAPWHS}
        </Typography>
      ),
    },
    {
      title: "WHS. Date",
      field: "MHWHSDA",
      editable: "never",
      type: "date",
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
          {item.MHWHSDA ? moment(item.MHWHSDA).format("DD/MM/YYYY") : ""}
        </Typography>
      ),
    },
    {
      title: "WHS. Remark",
      field: "MHWHSRE",
      editable: "never",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.MHWHSRE}
        </Typography>
      ),
    },

    {
      title: "ICT. Name",
      field: "MHAPICT",
      editable: "never",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.MHAPICT}
        </Typography>
      ),
    },
    {
      title: "ICT. Date",
      field: "MHICTDA",
      editable: "never",
      type: "date",
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
          {item.MHICTDA ? moment(item.MHICTDA).format("DD/MM/YYYY") : ""}
        </Typography>
      ),
    },
    {
      title: "ICT. Remark",
      field: "MHICTRE",
      editable: "never",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.MHICTRE}
        </Typography>
      ),
    },

    {
      title: "CIO. Name",
      field: "MHAPCIO",
      editable: "never",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.MHAPCIO}
        </Typography>
      ),
    },
    {
      title: "CIO. Date",
      field: "MHCIODA",
      editable: "never",
      type: "date",
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
          {item.MHCIODA ? moment(item.MHCIODA).format("DD/MM/YYYY") : ""}
        </Typography>
      ),
    },
    {
      title: "CIO. Remark",
      field: "MHCIORE",
      editable: "never",
      headerStyle: { maxWidth: 100, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.MHCIORE}
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
      title: "Status (M3)",
      field: "MGTRSL",
      editable: "never",
      headerStyle: { maxWidth: 30, whiteSpace: "nowrap", textAlign: "center" },
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
          {item.MGTRSL && item.MGTRSH ? item.MGTRSL + " : " + item.MGTRSH : ""}
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

  return (
    <div className={classes.root}>
      {/* MAR Head */}
      {/* <p>#Debug MARNumber {JSON.stringify(marnumber)}</p> */}
      {/* <p>#Debug MARHead {JSON.stringify(marhead)}</p> */}
      <Formik
        initialValues={{
          vMARNumber: "",
          vPrefix: "",
          vFromDate: "",
          vToDate: "",
          vStatus: "",
        }}
        onSubmit={(values, { setSubmitting }) => {
          // Set values before append data.
          values.vPrefix = marhead.vPrefix;
          // alert(JSON.stringify(values));

          let formData = new FormData();
          formData.append("vMARNumber", values.vMARNumber);
          formData.append("vPrefix", values.vPrefix);
          formData.append("vFromDate", values.vFromDate);
          formData.append("vToDate", values.vToDate);
          formData.append("vStatus", values.vStatus);

          if (values.vSubmit === "search") {
            setSuccess(false);
            setLoading(true);

            (async function() {
              await dispatch(
                marheadActions.getMARHeadMonitoring(
                  values.vPrefix ? values.vPrefix : null,
                  values.vMARNumber ? values.vMARNumber : null,
                  values.vFromDate ? values.vFromDate : null,
                  values.vToDate ? values.vToDate : null,
                  values.vStatus ? values.vStatus : null
                )
              );

              setSuccess(true);
              setLoading(false);
            })();
          }
        }}
      >
        {(props) => showForm(props)}
      </Formik>

      {/* MAR Detail */}
      {/* <p>#Debug Detail {JSON.stringify(itemmardetail)}</p> */}
      {/* <p>#Debug File {JSON.stringify(file)}</p> */}
      <MaterialTable
        id="root_mar"
        title={`MAR-${marhead.vPrefix} Number : ${marhead.vStatus}`}
        columns={columnheads}
        data={marheadReducer.result ? marheadReducer.result : []}
        isLoading={loading}
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
            icon: MailOutlineIcon,
            tooltip: "Resend email",
            iconProps: { color: "primary" },
            onClick: (event, rowData) => {
              // console.log("rowData: " + JSON.stringify([rowData]));
              setSuccess(false);
              setLoading(true);

              let data = [rowData];
              data.map((item) => {
                (async function() {
                  let data = await dispatch(
                    sendemailActions.reSendEmailMAR(
                      item.MHPREF,
                      item.MARNUMBER,
                      item.MHSTAT,
                      "Resend"
                    )
                  );

                  alert(JSON.stringify(data));

                  setSuccess(true);
                  setLoading(false);
                })();
              });
            },
          }),
        ]}
        editable={
          {
            // onRowUpdate: (newData, oldData) =>
            //   new Promise((resolve, reject) => {
            //     console.log("onRowUpdate: " + JSON.stringify(newData));
            //     let formData = new FormData();
            //     formData.append("vMARNumber", marhead.vMARNumber);
            //     formData.append("vPrefix", marhead.vPrefix);
            //     formData.append("vTypeAdjust", newData.vTypeAdjust);
            //     formData.append("vItemLine", newData.MLLINE);
            //     formData.append("vItem", newData.MLITNO);
            //     formData.append("vItemDesc1", newData.MMFUDS);
            //     formData.append("vFacility", newData.MLFACI);
            //     formData.append("vWarehouse", newData.MLWHLO);
            //     formData.append("vLocation", newData.MLWHSL);
            //     formData.append("vLotNo", newData.MLBANO);
            //     formData.append("vDate", newData.MLIDDT);
            //     formData.append("vUnit", newData.MMUNMS);
            //     formData.append("vQty", newData.MLQTY);
            //     formData.append("vUnitPrice", newData.MLPRIC);
            //     formData.append("vAmount", newData.AMT);
            //     formData.append("vRemark1", newData.MLREM1);
            //     formData.append("vRemark2", "");
            //     formData.append("vStatus", "00");
            //     // newData.AMT = newData.MLQTY * newData.MLPRIC
            //     (async function() {
            //       await dispatch(mardetailActions.updateMARDetail(formData));
            //       const dataUpdate = [...mardetail];
            //       const index = oldData.tableData.id;
            //       dataUpdate[index] = newData;
            //       setMARDetail([...dataUpdate]);
            //       handleCheckWHSApprove([...dataUpdate]);
            //       handleCheckSubmit([...dataUpdate]);
            //     })();
            //     resolve();
            //   }),
            // onRowDelete: (oldData) =>
            //   new Promise((resolve, reject) => {
            //     // console.log("onRowDelete: " + JSON.stringify(oldData));
            //     let data = [oldData];
            //     data.map((item) => {
            //       // console.log(item.ADRNUMBER + " : " + item.ALLINE);
            //       (async function() {
            //         await dispatch(
            //           mardetailActions.deleteMARDetail(
            //             marhead.vMARNumber,
            //             item.MLLINE
            //           )
            //         );
            //         const dataDelete = [...mardetail];
            //         const index = oldData.tableData.id;
            //         dataDelete.splice(index, 1);
            //         setMARDetail([...dataDelete]);
            //         handleCheckWHSApprove([...dataDelete]);
            //         handleCheckSubmit([...dataDelete]);
            //       })();
            //     });
            //     resolve();
            //   }),
          }
        }
      />
    </div>
  );
};
