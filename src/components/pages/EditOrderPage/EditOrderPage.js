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
import RefreshIcon from "@material-ui/icons/Refresh";
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
    vFacility: "",
    vOrderNumber: "",
    vSelectOrderNumber: "",
    vStatus: "",
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
    (async function() {
      await dispatch(
        marnumberActions.getOrderNumber(initialStateMARHead.vPrefix)
      );
    })();
  }, []);

  const marnumbers = useMemo(() =>
    marnumberReducer.result ? marnumberReducer.result : []
  );

  const accountants = useMemo(() =>
    accountantReducer.result ? accountantReducer.result : []
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
    } else {
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
    setMARNumber({ ...marnumber, vSelectOrderNumber: "" });

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
  };

  const handleDialogFileClose = () => {
    setOpenDialogFile(false);
    resetFileInput();
  };

  const handleRefresh = () => {
    dispatch(marnumberActions.getOrderNumber(initialStateMARHead.vPrefix));
  };

  const resetFileInput = () => {
    // 👇️ reset input value
    inputRef.current.value = null;
    setFile(false);
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
                <Grid item xs={12} md={3}>
                  <TextField
                    fullWidth
                    select
                    size="small"
                    variant="outlined"
                    id="vSelectMARNumber"
                    label="Order Number"
                    disabled={searchdisable}
                    value={marnumber.vSelectOrderNumber}
                    onChange={(event) => {
                      // console.log(event.target.value);
                      if (event.target.value) {
                        let getFullOrderNumber = event.target.value.split(":");
                        let getFacility = getFullOrderNumber[0].trim();
                        let getOrderNumber = getFullOrderNumber[1].trim();
                        let getLowStatus = getFullOrderNumber[2].trim();
                        let getHighStatus = getFullOrderNumber[3].trim();

                        setMARNumber({
                          ...marnumber,
                          vFacility: getFacility,
                          vOrderNumber: getOrderNumber,
                          vSelectOrderNumber: event.target.value,
                          vStatus: getLowStatus + " : " + getHighStatus,
                        });
                        handleSearch(getOrderNumber);
                      }
                    }}
                    InputLabelProps={{ shrink: true }}
                    SelectProps={{
                      native: true,
                    }}
                  >
                    <option />
                    {marnumbers.map((option) => (
                      <option key={option.ID} value={option.ORDERNUMBER}>
                        {option.ORDERNUMBER}
                      </option>
                    ))}
                  </TextField>
                </Grid>
                <Grid item xs={1} md={"auto"}>
                  <Button
                    style={{ minWidth: "1px" }}
                    size="large"
                    id="vRefresh"
                    variant="text"
                    // disabled={allocatedisable}
                    startIcon={<RefreshIcon />}
                    onClick={(event) => {
                      handleRefresh();
                    }}
                  />
                </Grid>
                <Grid item xs={6} md={"auto"} className={classes.wrapper}>
                  <Button
                    fullWidth
                    size="medium"
                    id="vCancelOrder"
                    variant="contained"
                    color="secondary"
                    type="submit"
                    startIcon={<DeleteIcon />}
                    disabled={canceldisable}
                    onClick={(event) => {
                      values.vSubmit = "cancel";
                    }}
                  >
                    Cancel
                  </Button>
                  {loadingcancel && (
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

              {/* {showfile ? showFile() : null} */}

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
          vFacility: "",
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
          values.vFacility = marnumber.vFacility;
          values.vOrderNumber = marnumber.vOrderNumber;
          values.vStatus = marnumber.vStatus;
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

          if (values.vSubmit === "cancel") {
            setCancelDisable(true);
            setSuccess(false);
            setLoadingCancel(true);

            (async function() {
              let data = await dispatch(
                marheadActions.cancelOrder(
                  values.vFacility,
                  values.vOrderNumber,
                  values.vStatus,
                  values.vSubmit
                )
              );
              alert(JSON.stringify(data));

              await dispatch(marnumberActions.getOrderNumber(values.vPrefix));
              handleClear();

              setSuccess(true);
              setLoadingCancel(false);
            })();
          }
        }}
      >
        {(props) => showForm(props)}
      </Formik>
    </div>
  );
};
