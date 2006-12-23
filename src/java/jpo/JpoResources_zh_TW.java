package jpo;

import java.util.*;

/*
JpoResources_cn.java:  class that holds the generic labels for the JPO application

Copyright (C) 2002-2006  Richard Eigenmann.
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or any later version. This program is distributed 
in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS 
FOR A PARTICULAR PURPOSE.  See the GNU General Public License for 
more details. You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
The license is in gpl.txt.
See http://www.gnu.org/copyleft/gpl.html for the details.

This file is edited by Franklin He (何前鋒) heqf@mail.cintcm.ac.cn
Date: 21-06-2006
*/


/**
 *  class that holds the generic labels for the JPO application.
 *  Use the following command to access your Strings:
 *  Settings.jpoResources.getString("key")
 */
public class JpoResources_zh_TW extends ListResourceBundle {
	public Object[][] getContents() {
		return contents;

	}
	public static final Object[][] contents = {
		// Jpo
		{"ApplicationTitle", "JPO - 圖片管理"},
		{"jpoTabbedPaneCollection", "圖片目錄"},
		{"jpoTabbedPaneSearches", "查找"},
		
	
		// Generic texts
         	{"genericTargetDirText", "目標文件夾："},
         	{"genericCancelText", "取消"},
		{"genericSaveButtonLabel", "保存"},
         	{"genericOKText", "确定"},             
		{"genericSelectText", "選擇"},
		{"threeDotText", "..."},
		{"genericExportButtonText", "導出"},
		{"genericSecurityException", "安全引起的錯誤"},
		{"genericError", "錯誤"},
		{"internalError", "或者"},
		{"genericWarning", "警告"},
		{"genericExit", "退出"},
		{"outOfMemoryError", "內存溢出"},
		{"areYouSure", "您确認所做的操作嗎？"},
		
		
		
		// Help About Dialog
		{"HelpAboutText", "JPO 版本 0.8.5 是基于Java/Swing 開發的軟件\n" 
			+ "作者Richard Eigenmann在瑞士蘇黎世, \n" 
			+ "版權 2000 - 2006\n"
			+ "郵件地址：richard.eigenmann@gmail.com\n"
			+ "軟件主頁网址：http://j-po.sourceforge.net\n"
			+ "\nExif（可交換圖像文件）的抽取部分蒙Drew Noakes的幫助\n"
			+  "表的排序部分蒙Philip Milne的幫助\n\n"},
		{"HelpAboutUser", "使用者：" },
		{"HelpAboutOs", "操作系統：" },
		{"HelpAboutJvm", "Java虛擬机" },
		{"HelpAboutJvmMemory", "Java虛擬机最大內存： " },
		{"HelpAboutJvmFreeMemory", "Java虛擬机可用內存： " },


		// QueryJFrame
		{"searchDialogTitle", "查找圖片"},
		{"searchDialogLabel", "查找"},
		{"searchDialogSaveResultsLabel", "保存搜索結果"},
		{"advancedFindJButtonOpen", "高級搜索條件"},
		{"advancedFindJButtonClose", "簡單搜索"},
		{"noSearchResults", "沒有符合條件的圖片。"},
		{"lowerDateJLabel", "在期間："},
		{"dateRangeError", "日期范圍指定無效"},
		


		
		// PictureViewer
		{"PictureViewerTitle", "JPO圖片瀏覽"},
		{"PictureViewerKeycodes", "可以使用以下的快捷方式\n" 
			+ "N: 下一張\n"
			+ "P: 前一張\n"
			+ "I: 關閉顯示圖片信息\n"
			+ "<space>,<home>: 适合窗口大小\n"
			+ "<left>,<right>,<up>,<down>: 圖片移位\n"
			+ "<PgUp>: 縮小\n"
			+ "<PgDown>: 放大\n"
			+ "1: 原始大小\n"
			+ "F: 窗口尺寸菜單\n"
			+ "M: 彈出菜單"},
		{"PictureViewerKeycodesTitle", "鍵盤快捷方式"},
		{"NavigationPanel", "導航工具"},
		{"fullScreenJButton.ToolTipText", "全屏"},
		{"popupMenuJButton.ToolTipText", "彈出菜單"},
		{"nextJButton.ToolTipText", "下一張圖片"},
		{"previousJButton.ToolTipText", "前一張圖片"},
		{"infoJButton.ToolTipText", "圖片信息"},
		{"resetJButton.ToolTipText", "重置"},
		{"clockJButton.ToolTipText", "自動播放"},
		{"closeJButton.ToolTipText", "關閉窗口"},
		{"rotateLeftJButton.ToolTipText", "左旋轉"},
		{"rotateRightJButton.ToolTipText", "右旋轉"},
		{"PictureViewerDescriptionFont", ""},

		// Settings
		{"SettingsTitleFont", ""},
		{"SettingsCaptionFont", ""},

		// SettingsDialog Texts
		{"settingsDialogTitle", "設置"},
		
		{"browserWindowSettingsJPanel", "一般項"},
		{"languageJLabel", "語言："},
		{"autoLoadJLabelLabel", "啟動加載："},
		{"logfileJCheckBoxLabel", "保存日志"},
		{"logfileJLabelLabel", "日志文件路徑以及文件名："},
		{"maximiseJpoOnStartupJCheckBoxLabel", "Maximise JPO window when program starts"},
		{"saveSizeJCheckBoxLabel", "在退出時保存窗口的位置"},
		{"MainCoordinates", "主窗口坐標(橫坐標/縱坐標):"},
		{"MainSize", "主窗口大小(寬度/高度):"},
		
		{"pictureViewerJPanel", "圖片瀏覽"},
		{"maximumPictureSizeLabel", "最大圖片縮放尺寸"},
		{"maxCacheLabel", "圖片最大緩存"},
		{"leaveSpaceLabel", "距离底部距离"},
		{"dontEnlargeJCheckBoxLabel", "不要放大小土篇"},
		{"pictureCoordinates", "默認圖片窗口坐標(橫坐標/縱坐標):"},
		{"pictureSize", "默認圖片窗口尺寸(寬度/高度):"},
		{"pictureViewerFastScale", "圖片自動播放速度"},
		
		{"thumbnailSettingsJPanel", "縮略圖"},
		{"thumbnailDirLabel", "縮略圖文件夾"},
		{"keepThumbnailsJCheckBoxLabel", "保存縮略圖"},
		{"maxThumbnailsLabelText", "每頁顯示縮略圖個數"},
		{"thumbnailSizeLabel", "縮略圖尺寸"},
		{"thumbnailFastScale", "縮略圖播放速度"},
		{"zapThumbnails", "刪除所有縮略圖"},
		{"thumbnailsDeleted", "縮略圖已經刪除"},
		
		{"autoLoadChooserTitle", "選擇啟動時加載的文件"},
		{"logfileChooserTitle", "選擇寫入日志的文件"},
		{"thumbDirChooserTitle", "選擇所略圖所在的文件夾"},
		
		{"settingsError", "配置錯誤"},
		{"generalLogFileError", "日志文件有錯誤，日志功能被禁用"},
		{"thumbnailDirError", "縮略圖所在文件夾出現嚴重錯誤"},

		{"userFunctionJPanel", "用戶自定義功能"},
		{"userFunction1JLabel", "用戶自定義功能1"},
		{"userFunction2JLabel", "用戶自定義功能2"},
		{"userFunction3JLabel", "用戶自定義功能3"},
		{"userFunctionNameJLabel", "名稱"},
		{"userFunctionCmdJLabel", "命令："},
		{"userFunctionHelpJTextArea", "%f將被文件名代替\n%u將被圖片所在URL代替"},

		{"emailJPanel", "郵件服務器"},
		{"emailJLabel", "郵件服務器詳細信息"},
		{"predefinedEmailJLabel", "預先配置的郵件服務器："},
		{"emailServerJLabel", "郵件服務器："},
		{"emailPortJLabel", "端口："},
		{"emailUserJLabel", "用戶名："},
		{"emailPasswordJLabel", "密碼："},
		
		
		
		// Settings
		{"thumbNoExistError", "縮略圖文件夾不存在。\n請選擇菜單編輯|配置來正确設置縮略圖文件夾\n縮略圖緩存功能已被禁用。"},
		{"thumbNoWriteError", "縮略圖文件夾不可寫。\n請選擇菜單編輯|配置來正确設置縮略圖文件夾\n縮略圖緩存功能已被禁用。"},
		{"thumbNoDirError", "縮略圖所在不是一個文件夾\n請選擇菜單編輯|配置來正确設置縮略圖文件夾\n縮略圖緩存功能已被禁用。"},
		{"logFileCanWriteError", "所配置的日志文件不可寫。\n請選擇菜單編輯|配置來正确設置日志文件\n日志功能已被禁用。"},
		{"logFileIsFileError", "所配置的日志文件不是一個文件。\n請選擇菜單編輯|配置來正确設置日志文件\n日志功能已被禁用。"},
		{"generalLogFileError", "日志文件有問題，日志功能被禁用。"},
		{"cantWriteIniFile", "啟動配置文件寫入出錯。\n"},
		{"cantReadIniFile", "讀配置文件JPO.ini失敗. 使用默認配置\n"},
		



		// HtmlDistillerJFrame
		{"HtmlDistillerJFrameHeading", "以HTML格式導出"},
		{"HtmlDistillerThreadTitle", "導出成HTML"}, 
		{"HtmlDistillerChooserTitle", "選擇保存HTML文件夾位置"},
		{"exportHighresJCheckBox", "導出清晰圖片"},
		{"linkToHighresJCheckBox", "使用當前位置鏈接到清晰圖片"},
		{"generateDHTMLJCheckBox", "生成DHTML鼠標移動效果"},
		{"generateZipfileJCheckBox", "生成清晰圖片可用于下載的壓縮文件包"},
		{"picsPerRowText", "列"},
		{"thubnailSizeJLabel", "所略圖尺寸"},
		{"htmlDistCrtDirError", "不能夠導出到文件夾！"},
		{"htmlDistIsDirError", "這不是一個文件夾！"},
		{"htmlDistCanWriteError", "這不是一個可寫的文件夾"},
		{"htmlDistIsNotEmptyWarning", "目標文件夾不為空\n點确認將繼續操作但可能覆蓋已有的文件。"},
		{"midresSizeJLabel", "清晰度滑尺"},
		{"midresJpgQualitySlider", "Jpg清晰度"},
		{"lowresJpgQualitySlider", "低清晰度Jpg"},
		{"jpgQualityBad", "低分辨率"},
		{"jpgQualityGood", "一般分辨率"},
		{"jpgQualityBest", "高清晰"},
		// HtmlDistillerThread
		{"LinkToJpo", "制作工具：<A HREF=http://j-po.sourceforge.net\">JPO</A>"},
		{"htmlDistillerInterrupt", "操作已被停止"},
		{"CssCopyError", "不能拷貝樣式文件jpo.css\n"},
		{"HtmlDistillerPreviewFont", ""},

				

		// ReconcileJFrame
		{"ReconcileJFrameTitle", "圖片集与文件夾相應文件檢查"},
		{"ReconcileBlaBlaLabel", "<HTML>檢查文件夾中的文件是否出現在圖片集中</HTML>"},
		{"directoryJLabelLabel", "需要檢查的文件夾"},
		{"directoryCheckerChooserTitle", "選擇需要檢查的文件夾"},
		{"ReconcileFound", "在圖片集中監測到的文件"},
		{"ReconcileNotFound", "不在圖片集中的有： "},
		{"ReconcileDone", "完成檢查\n"},
		{"ReconcileInterrupted", "檢查被中斷\n"},
		{"ReconcileListPositives", "列出相匹配的"},
		{"ReconcileOkButtonLabel", "匹配檢查"},
		{"ReconcileSubdirectories", "匹配檢查所有子文件夾"},
		{"ReconcileCantReadError", "不能夠讀 "},
		{"ReconcileNullFileError", "文件夾不正确"},
		{"ReconcileStart", "匹配檢查文件夾"},
		{"ReconcileNoFiles", "沒有發現文件\n"},

		
		// CollectionDistillerJFrame
		{"CollectionDistillerJFrameFrameHeading", "導成新的圖片集"},
		{"collectionExportPicturesText", "導出圖片"},
		{"xmlFileNameLabel", "XML文件名稱："},
		{"collectionExportChooserTitle", "選擇導出到文件夾"},


		// ConsolidateGroupJFrame
		{"highresTargetDirJTextField", "選擇清晰圖片整合目錄"},
		{"lowresTargetDirJTextField", "選擇低分辨率圖片整合目錄"},
		{"RecurseSubgroupsLabel", "對所有的子目錄操作"},
		{"ConsolidateGroupBlaBlaLabel", "<HTML>此功能將所有選中的圖片組移動到指定的文件夾。這將自動修复文件組中的圖片引用位置，所有的文件會在磁盤上移動<br><p> <font color=red>您确認要這樣操作嗎？<br></font></htm>"},
		{"ConsolidateGroupJFrameHeading", "整合/移動圖片"},
		{"ConsolidateButton", "整合"},
		{"ConsolidateFailure", "圖片整合失敗，退出"},
		{"ConsolitdateProgBarTitle", "圖片正在整合中"},
		{"ConsolitdateProgBarDone", "圖片集整合完畢"},
		{"lowresJCheckBox", "連同低分辨率圖片也整合"},
		

		
		// JarDistillerJFrame
		{"groupExportJarTitleText", "導出成Jar格式的包"},
		{"JarDistillerLabel", "創建Jar (Java包)："},
		{"SelectJarFileTitle", "選擇Jar包導出的文件夾位置："},
		
		// PictureInfoEditor
		{"PictureInfoEditorHeading", "圖片屬性"},
		{"highresChooserTitle", "選擇高清晰度圖片"},
		{"pictureDescriptionLabel", "圖片名稱："},
		{"creationTimeLabel", "創建時間："},
		{"highresLocationLabel", "高清晰度圖片位置："},
		{"lowresLocationLabel", "低清晰度圖片位置；"},
		{"filmReferenceLabel", "影像位置："},
		{"rotationLabel", "在打開時旋轉圖片："},
		{"commentLabel", "描述："},
		{"copyrightHolderLabel", "版權信息："},
		{"photographerLabel", "攝影作者："},
		{"resetLabel", "重置"},
		{"checksumJButton", "刷新"},
		{"checksumJLabel", "Adler32校檢和"},
		{"parsedAs", "解析成： "},
		{"failedToParse", "不能解析日期"},
		{"categoriesJLabel-2", "類目："},
		{"setupCategories", ">>設置類目<<"},
		{"noCategories", ">> 不屬于任何類目 <<"},

		
		//GroupInfoEditor
		{"GroupInfoEditorHeading", "編輯圖片組描述"},
		{"groupDescriptionLabel", "圖片組描述"},
		
		// GroupPopupMenu
		{"groupShowJMenuItem", "顯示圖片組"},
		{"groupSlideshowJMenuItem", "顯示圖片"},
		{"groupFindJMenuItemLabel", "查找"},
		{"groupEditJMenuItem", "重命名"},
		{"groupRefreshJMenuItem", "刷新圖標"},
		{"groupTableJMenuItemLabel", "以表格方式編輯"},
		{"addGroupJMenuLabel", "添加圖片組"},
		{"addNewGroupJMenuItemLabel", "新建圖片組"},
		{"addPicturesJMenuItemLabel", "圖片"},
		{"addCollectionJMenuItemLabel", "圖片集"},
		{"groupExportNewCollectionMenuText", "導出成圖片集"},
		{"addFlatFileJMenuItemLabel", "無格式文件"},
		{"moveNodeJMenuLabel", "移動"},
		{"moveGroupToTopJMenuItem", "置頂"},
		{"moveGroupUpJMenuItem", "向上"},
		{"moveGroupDownJMenuItem", "向下"},
		{"moveGroupToBottomJMenuItem", "置底"},
		{"indentJMenuItem", "相內"},
		{"outdentJMenuItem", "向外"},
		{"groupRemoveLabel", "移除"},
		{"consolidateMoveLabel", "整合/移動"},
		{"sortJMenu", "排序方式"},
		{"sortByDescriptionJMenuItem", "名稱"},
		{"sortByFilmReferenceJMenuItem", "影像位置"},
		{"sortByCreationTimeJMenuItem", "創建日期"},
		{"sortByCommentJMenuItem", "描述"},
		{"sortByPhotographerJMenuItem", "攝影作者"},
		{"sortByCopyrightHolderTimeJMenuItem", "版權"},
		{"groupExportHtmlMenuText", "導出成HTML"},
		{"groupExportFlatFileMenuText", "導出成無格式文件"},
		{"groupExportJarMenuText", "導出成Jar壓縮包"},
		
		
		// PicturePopupMenu
		{"pictureShowJMenuItemLabel", "顯示圖片"},
		{"pictureEditJMenuItemLabel", "屬性"},
		{"copyImageJMenuLabel", "复制圖片"},
		{"copyToNewLocationJMenuItem", "選擇复制的文件夾位置"},
		{"FileOperations", "文件操作"},
		{"fileRenameJMenuItem", "重命名"},
		{"FileRenameLabel1", "重命名 \n"},
		{"FileRenameLabel2", "\n成: "},
		{"fileDeleteJMenuItem", "刪除"},
		{"pictureRefreshJMenuItem", "刷新縮略圖"},
		{"pictureMailSelectJMenuItem", "選中發送郵件"},
		{"pictureMailUnselectJMenuItem", "選擇不用來發送郵件"},
		{"pictureMailUnselectAllJMenuItem", "清除要發送郵件的選擇"},
		{"rotation", "翻轉"},
		{"rotate90", "右轉90度"},
		{"rotate180", "旋轉180度"},
		{"rotate270", "左轉270度"},
		{"rotate0", "不旋轉"},
		{"userFunctionsJMenu", "用戶自定義動能"},
		{"pictureNodeRemove", "刪除圖片"},
		{"movePictureToTopJMenuItem", "置頂"},
		{"movePictureUpJMenuItem", "向上移動"},
		{"movePictureDownJMenuItem", "向下移動"},
		{"movePictureToBottomJMenuItem", "置底"},
		{"recentDropNodePrefix", "移到圖組： "},
		{"categoryUsagetJMenuItem", "類目"},


		// ThumbnailJScrollPane
		{"ThumbnailSearchResults", "搜索結果"},
		{"ThumbnailSearchResults2", "在"},
		{"ThumbnailToolTipPrevious", "前一頁"},
		{"ThumbnailToolTipNext", "后一頁"},
		{"ThumbnailJScrollPanePage", "頁"},

		//ChangeWindowPopupMenu
		{"fullScreenLabel", "全屏"},
		{"leftWindowLabel", "左"},
		{"rightWindowLabel", "右"},
		{"topLeftWindowLabel", "左上"},
		{"topRightWindowLabel", "右上"},
		{"bottomLeftWindowLabel", "左下"},
		{"bottomRightWindowLabel", "右下"},
		{"defaultWindowLabel", "默認"},
		{"windowDecorationsLabel", "窗體"},
		{"windowNoDecorationsLabel", "沒有窗體"},


		// CollectionJTree
		{"DefaultRootNodeText", "新建圖片集"},
		{"CopyImageDialogButton", "复制"},
		{"CopyImageDialogTitle", "指定复制的位置："},
		{"CopyImageNullError", "复制文件夾的參數無效，复制終止。"},
		{"CopyImageDirError", "不能創建目標文件夾，复制終止。\n"},
		{"fileOpenButtonText", "打開"},
		{"fileOpenHeading", "打開圖片集"},
		{"fileSaveAsTitle", "另存為圖片集"},
		{"collectionSaveTitle", "保存圖片集"},
		{"collectionSaveBody", "另存圖片集\n"},
		{"addSinglePictureTitle", "選擇需要添加的圖片"},
		{"addSinglePictureButtonLabel", "選擇"},
		{"addFlatFileTitle", "選擇無格式圖片文件列表"},
		{"saveFlatFileTitle", "以無格式方式保存圖片集"},
		{"saveFlatFileButtonLabel", "保存"},
		{"moveNodeError", "移動目標在同一文件夾下，移動無效。"},
		{"unsavedChanges", "不保存退出。"},
		{"confirmSaveAs", "文件已經存在\n要覆蓋保存嗎？"},
		{"discardChanges", "放棄"},
		{"noPicsForSlideshow", "圖片組中沒有圖片"},
		{"fileRenameTitle", "重命名文件"},
		{"fileDeleteTitle", "刪除文件"},
		{"fileDeleteError", "不能夠刪除文件\n"},
		{"deleteRootNodeError", "根圖片集不能被刪除"},
				
		// ApplicationJMenuBar
		{"FileMenuText", "文件"},
		{"FileNewJMenuItem", "新建圖片集"},
		{"FileLoadMenuItemText", "打開圖片集"},
		{"FileOpenRecentItemText", "打開最近使用文件"},
		{"FileAddMenuItemText", "添加圖片"},
		{"FileCameraJMenuItem", "從相机中添加圖片"},
		{"FileSaveMenuItemText", "保存圖片集"},
		{"FileSaveAsMenuItemText", "另存為"},
		{"FileExitMenuItemText", "退出"},
		{"EditJMenuText", "編輯"},
		{"EditFindJMenuItemText", "查找"},
		{"EditCheckDirectoriesJMenuItemText", "檢查"},
		{"EditCollectionPropertiesJMenuItem", "圖片集屬性"},
		{"EditCheckIntegrityJMenuItem", "完整性檢查"},
		{"EditCamerasJMenuItem", "相机"},
		{"EditCategoriesJMenuItem", "類目"},
		{"EditSettingsMenuItemText", "設置..."},
		{"actionJMenu", "工具"},
		{"emailJMenuItem", "發送郵件"},
		{"HelpJMenuText", "幫助"},
		{"HelpAboutMenuItemText", "關于"},
		{"HelpLicenseMenuItemText", "授權"},
		
		// PictureViewer
		{"autoAdvanceDialogTitle", "自動播放"},
		{"randomAdvanceJRadioButtonLabel", "任意順序播放"},
		{"sequentialAdvanceJRadioButtonLabel", "順序播放"},
		{"restrictToGroupJRadioButtonLabel", "限制在本圖片組內播放"},
		{"useAllPicturesJRadioButtonLabel", "循環播放所有圖片"},
		{"timerSecondsJLabelLabel", "播放時延(秒)"},

		// ExifViewerJFrame		
		{"ExifTitle", "EXIF圖片交換格式頭\n"},
		{"noExifTags", "沒有發現EXIF圖片交換格式標簽"},
		
		// PictureAdder
		{"PictureAdderDialogTitle", "添加圖片以及文件夾"},
		{"PictureAdderProgressDialogTitle", "正在添加圖片"},
		{"notADir", "不是一個文件夾\n"},
		{"notGroupInfo", "不是圖片組"},
		{"fileChooserAddButtonLabel", "添加"},
		{"recurseSubdirectoriesTitle", "應用于所有的子文件夾"},
		{"recurseSubdirectoriesMessage", "您的選擇包含了子文件夾，\n將所有的子文件夾也加入嗎？"},
		{"recurseSubdirectoriesOk", "添加"},
		{"recurseSubdirectoriesNo", "否"},
		{"picturesAdded", "圖片已經添加"},
		{"pictureAdderOptionsTab", "選項"},
		{"pictureAdderThumbnailTab", "所略圖"},
		{"pictureAdderCategoryTab", "類目"},

		// AddFromCamera
		{"AddFromCamera", "從相机中添加圖片"},
		{"cameraNameJLabel", "相机名稱："},
		{"cameraDirJLabel", "相机在文件系統中的位置："},
		{"cameraConnectJLabel", "連接相机的命令："},
		{"cameraDisconnectJLabel", "斷開相机連接的命令："},
		{"allPicturesJRadioButton", "把相机中的所有圖片都加入圖片集"},
		{"newPicturesJRadioButton", "只從相机中添加新的圖片"},
		{"missingPicturesJRadioButton", "添加不在圖片集中的文件"},
		{"targetDirJLabel", "圖片的目標文件夾："},
		{"AddFromCameraOkJButton", "運行"},
		{"editCameraJButton", "編輯相机"},
		{"categoriesJButton", "類目"},
		
		// CameraEditor
		{"CameraEditor", "編輯相机設置"},
		{"cameraNewNameJLabel", "新建相机名"},
		{"runConnectJButton", "運行"},
		{"saveJButton", "保存"},
		{"memorisedPicsJLabel", "上次導入的圖片數："},
		{"refreshJButton", "刷新"},
		{"zeroJButton", "取消"},
		{"addJButton", "添加"},
		{"deleteJButton", "刪除"},
		{"closeJButton", "關閉"},
		{"filenameJCheckBox", "以文件名方式記憶圖片"},
		{"refreshJButtonError", "請先保存您的變更！"},
		
		

		// Camera
		{"countingChecksum", "正在創建校檢和"},
		{"countingChecksumComplete", "計算完成校檢和"},
		{"newCamera", "新建相机"},
		
		
		

		// XmlDistiller
		{"DtdCopyError", "不能夠复制collection.dtd配置文件\n"},

		// CollectionProperties
		{"CollectionPropertiesJFrameTitle", "圖片集屬性"},
		{"CollectionNodeCountLabel", "圖片及圖組總數："},
		{"CollectionGroupCountLabel", "圖組數："},
		{"CollectionPictureCountLabel", "圖片數： "},
		{"CollectionSizeJLabel", "占用磁盤空間："},
		{"queCountJLabel", "待處理的縮略圖數: "},
		{"editProtectJCheckBoxLabel", "圖片集寫保護"},
		
		// Tools
		{"copyPictureError1", "不能复制\n"},
		{"copyPictureError2", "\n到： "},
		{"copyPictureError3", "\n原因： "},
		{"freeMemory", "可用內存空間： "},

		// PictureAdder
		{"recurseJCheckBox", "應用到所有子文件夾"},
		{"retainDirectoriesJCheckBox", "保持目錄結构"},
		{"newOnlyJCheckBox", "只添加新的圖片"},
		{"showThumbnailJCheckBox", "顯示縮略圖"},

		// IntegrityChecker
		{"IntegrityCheckerTitle", "檢查圖片集完整性"},
		{"integrityCheckerLabel", "正在檢查完整性"},
		{"check1", "檢查日期格式"},
		{"check1done", "日期格式不正确"},
		{"check2", "校檢和檢驗"},
		{"check2progress", "正在運行校檢和修复: "},
		{"check2done", "修复后的校檢和 "},
		{"check3", "檢查3"},
		
		// SortableDefaultMutableTreeNode
		{"GDPMdropBefore", "拖放在目標前"},
		{"GDPMdropAfter", "拖放在目標后"},
		{"GDPMdropIntoFirst", "拖放到最頂端"},
		{"GDPMdropIntoLast", "拖放到底部"},
		{"GDPMdropCancel", "取消拖放"},
		{"copyAddPicturesNoPicturesError", "沒有找到圖片，操作退出。"},
		{"FileDeleteTitle", "刪除"},
		{"FileDeleteLabel", "刪除文件\n"},
		{"newGroup", "新建圖組"},
		{"queriesTreeModelRootNode", "查找"},


		// CategoryEditorJFrame
		{"CategoryEditorJFrameTitle", "類目編輯器"},
		{"categoryJLabel", "類目"},
		{"categoriesJLabel", "類目"},
		{"addCateogryJButton", "添加類目"},
		{"deleteCateogryJButton", "刪除類目"},
		{"renameCateogryJButton", "重命名類目"},
		{"doneJButton", "完成"},
		{"countCategoryUsageWarning1", "共計"},
		{"countCategoryUsageWarning2", "還有屬于該類的圖片\n确信要刪除該類嗎？"},

		// CategoryUsageJFrame
		{"CategoryUsageJFrameTitle", "類使用狀況"},
		{"numberOfPicturesJLabel", "圖片被選中"},
		{"updateJButton", "更新"},
		{"refreshJButtonCUJF", "刷新"},
		{"modifyCateogryJButton", "類目"},
		{"cancelJButton", "取消"},

		
		// EmailerJFrame
		{"EmailerJFrame", "發送郵件"},
		{"imagesCountJLabel", "選中的圖片數： "},
		{"emailJButton", "發送"},
		{"noNodesSelected", "沒有選中的圖片，請在相應圖片上按右鍵彈出菜單來選擇"},
		{"fromJLabel", "從："},
		{"toJLabel", "到："},
		{"messageJLabel", "信息："},
		{"subjectJLabel", "主題："},
		{"emailSendError", "發生了如下的錯誤：\n"},
		{"emailOK", "郵件發送成功"},
		{"emailSizesJLabel", "郵件大小："},
		{"emailResizeJLabel", "郵件大小變成："},
		{"emailSize1", "小圖片 (350 x 300)"},
		{"emailSize2", "中等圖片 (700 x 550)"},
		{"emailSize3", "原始圖片大小"},
		{"emailSize4", "大圖片大小 (1000 x 800)"},
		{"emailSize5", "原始大小"},
		{"emailOriginals", "附上原始圖片"},
		{"emailNoNodes", "沒有需要發送的圖片，請用右彈出菜單在相應圖片上選擇"},
		{"emailNoServer", "沒有配置郵件服務器。在 編輯 > 設置... > 郵件服務器 選項中配置服務器。"},

		//Emailer Thread
		{"EmailerLoading", "正在裝入："},
		{"EmailerScaling", "正在改變大小： "},
		{"EmailerWriting", "正在寫入"},
		{"EmailerAdding", "正在添加"},
		{"EmailerSending", "正在發送郵件"},
		{"EmailerSent", "郵件已經分發到服務器"},

		//CategoryQuery
		{"CategoryQuery", "類目查詢"},

		//PicturePanel
		{"PicturePaneInfoFont", ""},   
		{"PicturePaneSize", "尺寸："}, 
		{"PicturePaneMidpoint", " 中心點"}, 
		{"PicturePaneLoadTime", "載入："}, 
		{"PicturePaneSeconds", "秒"}, 
		{"PicturePaneFreeMemory", "可用內存空間："}, 
		{"PicturePaneReadyStatus", "就緒"},

		//ExifInfo
		{"ExifInfoCamera", "照相机："},
		{"ExifInfoLens", "鏡頭："},
		{"ExifInfoShutterSpeed", "快門速度："},
		{"ExifInfoAperture", "光圈："},
		{"ExifInfoFocalLength", "焦距："},
		{"ExifInfoISO", "感光度ISO："},
		{"ExifInfoTimeStamp", "拍攝時間："},

		//ThumbnailDescriptionJPane

		{"ThumbnailDescriptionJPanelLargeFont", ""},
		{"ThumbnailDescriptionJPanelSmallFont", ""},
		{"ThumbnailDescriptionNoNodeError", "此位置上沒有圖片。"},

		// ScalablePicture
		{"ScalablePictureUninitialisedStatus", "沒有初始化"},
		{"ScalablePictureLoadingStatus", "正在裝入"},
		{"ScalablePictureRotatingStatus", "正在翻轉"},
		{"ScalablePictureScalingStatus", "正在縮放"},


		{"Template", "模板"}
		
		
	};
}
