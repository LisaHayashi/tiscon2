
package net.unit8.sigcolle.controller;

import javax.inject.Inject;
import javax.transaction.Transactional;

import enkan.component.doma2.DomaProvider;
import enkan.data.Flash;
import enkan.data.HttpResponse;
import kotowari.component.TemplateEngine;
import net.unit8.sigcolle.dao.CampaignDao;
import net.unit8.sigcolle.dao.SignatureDao;
import net.unit8.sigcolle.form.CampaignForm;
import net.unit8.sigcolle.form.SignatureForm;
import net.unit8.sigcolle.model.Campaign;
import net.unit8.sigcolle.model.UserCampaign;
import net.unit8.sigcolle.model.Signature;

import static enkan.util.BeanBuilder.builder;
import static enkan.util.HttpResponseUtils.RedirectStatusCode.SEE_OTHER;
import static enkan.util.HttpResponseUtils.redirect;
import static enkan.util.ThreadingUtils.some;

/**
 * @author kawasima
 */
public class CampaignController {
    @Inject
    private TemplateEngine templateEngine;

    @Inject
    private DomaProvider domaProvider;

    private HttpResponse showCampaign(Long campaignId, SignatureForm signature, String message) {
        CampaignDao campaignDao = domaProvider.getDao(CampaignDao.class);
        UserCampaign campaign = campaignDao.selectById(campaignId);
//        UserCampaign 値を入れておく場所（エンティティ） = クラス名.関数名(渡す値);

        SignatureDao signatureDao = domaProvider.getDao(SignatureDao.class);
        int signatureCount = signatureDao.countByCampaignId(campaignId);

        //renderメッソドは色々やってくれる
        //HTMLを作ってレスポンスを返してくれる
        return templateEngine.render("campaign",//全体の骨組みは決めておく
                "campaign", campaign,//二つ目以降には必要なものを書いていく
                "signatureCount", signatureCount,//骨組みの名前はtemplateのhtmlに書いておく
                "signature", signature,
                "message", message
        );
    }

    /**
     * キャンペーン詳細画面表示.
     * @param form URLパラメータ
     * @param flash flash scope session
     * @return HttpResponse
     */

    //ルートで呼び出されるメソッド、関数の処理内容の定義
    public HttpResponse index(CampaignForm form, Flash flash) {
        if (form.hasErrors()) {
            return builder(HttpResponse.of("Invalid"))
                    .set(HttpResponse::setStatus, 400)
                    .build();
        }

        //残りの処理はshowCampaignメソッドに任せるよって意味
        return showCampaign(form.getCampaignIdLong(),
                new SignatureForm(),
                (String) some(flash, Flash::getValue).orElse(null));
    }

    /**
     * 署名の追加処理.
     * @param form 画面入力された署名情報.
     * @return HttpResponse
     */
    @Transactional
    public HttpResponse sign(SignatureForm form) {
        if (form.hasErrors()) {
            return showCampaign(form.getCampaignIdLong(), form, null);
        }
        Signature signature = builder(new Signature())
                .set(Signature::setCampaignId, form.getCampaignIdLong())
                .set(Signature::setName, form.getName())
                .set(Signature::setSignatureComment, form.getSignatureComment())
                .build();
        SignatureDao signatureDao = domaProvider.getDao(SignatureDao.class);
        signatureDao.insert(signature);

        return builder(redirect("/campaign/" + form.getCampaignId(), SEE_OTHER))
                .set(HttpResponse::setFlash, new Flash("ご賛同ありがとうございました！"))
                .build();
    }

    /**
     * 新規キャンペーン作成画面表示.
     * @return HttpResponse
     */
    public HttpResponse createForm() {
        return templateEngine.render("signature/new");
    }

    public HttpResponse createFormlist() {
        return templateEngine.render("signature/list");
    }

    /**
     * 新規キャンペーン作成処理.
     * @return HttpResponse
     */
    public HttpResponse create(CampaignForm form) {
        // TODO: create campaign
        // ***********************************************↓

        Campaign campaign = builder(new Campaign())
                .set(Campaign::setCampaignId, form.getCampaignId())
                .set(Campaign::setTitle, form.getTitle())
                .set(Campaign::setStatement, form.getStatement())
                .set(Campaign::setGoal, form.getGoal())
                .set(Campaign::setCreateUserId, form.getUserId())
                .build();
        CampaignDao campaignDao = domaProvider.getDao(CampaignDao.class);
        campaignDao.insert(campaign);

        return builder(redirect("/campaign/", SEE_OTHER))
                .set(HttpResponse::setFlash, new Flash("新規キャンペーンを登録しました"))
                .build();
        //***********************************************↑

    }
}
