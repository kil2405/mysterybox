package com.weplaylabs.mysterybox.controller.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;

import com.weplaylabs.mysterybox.common.ConstantVal;
import com.weplaylabs.mysterybox.common.ExFun;
import com.weplaylabs.mysterybox.common.GameResource;
import com.weplaylabs.mysterybox.common.TimeCalculation;
import com.weplaylabs.mysterybox.common.WeException;
import com.weplaylabs.mysterybox.controller.contents.ContentsService;
import com.weplaylabs.mysterybox.controller.rank.RankingService;
import com.weplaylabs.mysterybox.model.game.ItemReward;
import com.weplaylabs.mysterybox.model.game.User;
import com.weplaylabs.mysterybox.model.game.UserConnectLog;
import com.weplaylabs.mysterybox.model.game.UserItem;
import com.weplaylabs.mysterybox.model.resource.CountryResource;
import com.weplaylabs.mysterybox.model.resource.DailyItemAppearResource;
import com.weplaylabs.mysterybox.repository.game.ItemRewardJpaService;
import com.weplaylabs.mysterybox.repository.game.UserConnectLogRepository;
import com.weplaylabs.mysterybox.repository.game.UserItemJpaService;
import com.weplaylabs.mysterybox.repository.game.UserJpaService;
import com.weplaylabs.mysterybox.request.ReqCreateNickname;
import com.weplaylabs.mysterybox.request.ReqLogin;
import com.weplaylabs.mysterybox.request.ReqTermsAgree;
import com.weplaylabs.mysterybox.response.BaseResponse;
import com.weplaylabs.mysterybox.response.ResLogin;
import com.weplaylabs.mysterybox.response.ResRankingUsers;
import com.weplaylabs.mysterybox.util.MapperVO;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService
{
    @Value("${spring.google.login_verify_url}")
    private String google_url;

    @Value("${server.terms.url}")
    private String terms_url;

    @Autowired
    private UserJpaService userJpaService;

    @Autowired
    private ItemRewardJpaService itemRewardJpaService;

    @Autowired
    private UserItemJpaService userItemJpaService;

    @Autowired
    private GameResource gameResource;

    @Autowired
    private MapperVO mapperVO;

    @Autowired
    private RankingService rankingService;

    @Autowired
    private ContentsService contentsService;

    @Autowired
    private UserConnectLogRepository userConnectLogRepository;

    @Autowired
    private ExFun exFun;

    public ResLogin UserLogin(ReqLogin req) throws Exception
    {
        String id = "";
        switch (req.platform)
        {
            case ConstantVal.MARKET_TYPE_AOS -> id = googleLoginVerify(req.accessToken);
            case ConstantVal.MARKET_TYPE_IOS -> id = iosLoginVerify(req.accessToken);
            default -> throw new WeException(ConstantVal.ERROR_CDOE_FILTER_1004);
        }

        if(id.isEmpty())
        {
            // 구글 로그인 검증 실패
            throw new WeException(ConstantVal.ERROR_CDOE_FILTER_1004);
        }

        HttpServletRequest servlet = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        String clientIP = exFun.getClientIP(servlet);
        String clientRegion = exFun.getClientRegion(clientIP);

        CountryResource countryRS = gameResource.getCountry().get(clientRegion);
        if(countryRS == null)
            clientRegion = "ZZ";
        
        User user = userJpaService.findByUserUid(id);
        if(user == null)
        {
            // 신규 가입 시작
            user = createAccount(id, clientRegion, req.language, req.isGuest);
            initClickRewardCount(user);
        }
        
        String encryption = UUID.randomUUID().toString().replace("-", "");
        encryption = encryption.substring(0, 16);
        user.setEncryption(encryption);
        user.setRegion(clientRegion);

        boolean dayChanged = !TimeCalculation.checkToday((int)user.getLoginTime());
        if(dayChanged)
        {
            // 접속 후 하루가 지났다.
            initClickRewardCount(user);
        }

        user.setLoginTime(TimeCalculation.getCurrentUnixTime());
        
        List<UserItem> userItems = userItemJpaService.findAllByUserId(user.getUserId());

        ResRankingUsers rankRes = rankingService.getUserRankingList(user.getUserId());
        if(rankRes == null)
            throw new WeException(ConstantVal.ERROR_CDOE_FILTER_1004);

        ResLogin res = new ResLogin();
        res.result = ConstantVal.DEFAULT_SUCCESS;
        res.items = mapperVO.makeItemVO(userItems);
        res.country = new ArrayList<>(rankRes.country);
        res.personal = new ArrayList<>(rankRes.personal);
        res.userRecord = rankRes.userRecord;
        res.rewardGold = contentsService.getIdleaGold(user, false);
        res.account = mapperVO.makeAccountVO(user);
        res.termsUrl = user.isTermsAgree() == false ? terms_url : null;

        userJpaService.save(user);

        // 접속 로그 추가
        UserConnectLog connectLog = UserConnectLog.builder()
            .userId(user.getUserId())
            .logDate(TimeCalculation.getCurrentUnixTime())
            .clickCount(user.getClickCount())
            .gold(user.getGold())
            .clientIp(clientIP)
            .build();
        
        userConnectLogRepository.save(connectLog);
        
        return res;
    }

    public BaseResponse TermsAgree(int userId, ReqTermsAgree req) throws Exception
    {
        User user = userJpaService.findByUserId(userId);
        if(user == null)
        {
            throw new WeException(ConstantVal.ERROR_CDOE_FILTER_1004);
        }

        user.setTermsAgree(req.termsAgree);
        userJpaService.save(user);

        BaseResponse res = new BaseResponse();
        res.result = ConstantVal.DEFAULT_SUCCESS;
        return res;
    }

    public BaseResponse CreateNickname(int userId, ReqCreateNickname req) throws Exception
    {
        if(req.nickname == null || req.nickname.isEmpty())
            throw new WeException(ConstantVal.ERROR_CDOE_FILTER_1004);

        User user = userJpaService.findByUserId(userId);
        if(user == null)
            throw new WeException(ConstantVal.ERROR_CDOE_FILTER_1004);

        // 이미 닉네임이 존재하는 경우
        if(user.getUserNickname() != null && !user.getUserNickname().isEmpty())
            throw new WeException(ConstantVal.ERROR_CDOE_FILTER_1004);

        String nickname = userJpaService.findByUserNickname(req.nickname);
        if(nickname != null)
            throw new WeException(ConstantVal.ERROR_CDOE_USER_1005);

        // 닉네임 글자수 체크
        int nicknameLength = exFun.getStringLength(req.nickname);
        if(nicknameLength < 2 || nicknameLength > 12) 
            throw new WeException(ConstantVal.ERROR_CDOE_USER_1006);
        
        // 닉네임 한글/영문/숫자만 입력 가능
        boolean checkInput = exFun.checkInputOnlyNumberAndAlphabet(req.nickname);
        if(!checkInput)
            throw new WeException(ConstantVal.ERROR_CDOE_USER_1006);

        user.setUserNickname(req.nickname);
        userJpaService.save(user);

        BaseResponse res = new BaseResponse();
        res.result = ConstantVal.DEFAULT_SUCCESS;
        return res;
    }

    private String googleLoginVerify(String accessToken)
    {
        try
        {
            WebClient webClient = WebClient.builder().baseUrl(google_url).build();
            Map<String, Object> response = webClient.get().uri(uriBuilder -> uriBuilder.path("/userinfo/v2/me")
                .queryParam("access_token", accessToken)
                .build()).retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    
            return (String) response.get("id");
        }
        catch(Exception e)
        {
            return new String();
        }
    }

    private String iosLoginVerify(String accessToken)
    {
        return new String();
    }

    private User createAccount(String uid, String region, String language, byte isGuest)
    {
        User userEntity = User.builder()
            .userUid(uid)
            .userNickname("")
            .encryption("")
            .wallet("")
            .termsAgree(false)
            .userGrade(ConstantVal.USER_GRADE_NORMAL)
            .gold(ConstantVal.DEFAULT_ZERO)
            .clickCount(ConstantVal.DEFAULT_ZERO)
            .isGuest(isGuest)
            .language(language)
            .region(region)
            .loginTime(TimeCalculation.getCurrentUnixTime())
            .build();

        // Auto Increment 값 얻어옴. (userId)
        int id = userJpaService.save(userEntity).getUserId();
        userEntity.setUserId(id);

        return userEntity;
    }

    private void initClickRewardCount(User user) throws Exception
    {
        List<DailyItemAppearResource> appears = gameResource.getDailyItemAppear().getAll();
        if(appears == null || appears.isEmpty())
            throw new WeException(ConstantVal.ERROR_CDOE_FILTER_1004);

        List<ItemReward> rewards = itemRewardJpaService.findAllByUserId(user.getUserId());
        if(rewards.isEmpty())
        {
            rewards = new ArrayList<>();
            for(int i = 0; i < appears.size(); i++)
            {
                ItemReward reward = ItemReward.builder()
                    .userId(user.getUserId())
                    .appearNum(i + 1)
                    .count(0)
                    .isReward(false)
                    .build();
                    
                rewards.add(reward);
            }
        }

        if (appears.size() != rewards.size())
        {
            for (int i = 0; i < appears.size(); i++)
            {
                // 중복 여부를 확인하기 위한 플래그
                boolean isDuplicate = false;
        
                // rewards에 이미 존재하는지 확인
                for (ItemReward reward : rewards) {
                    // 만약 rewards 리스트에 같은 appearNum이 있다면 중복 처리
                    if (reward.getAppearNum() == (i + 1)) {
                        isDuplicate = true;
                        break;
                    }
                }
        
                // 중복이 없으면 새로운 보상 추가
                if (!isDuplicate)
                {
                    ItemReward reward = ItemReward.builder()
                        .userId(user.getUserId())
                        .appearNum(i + 1)
                        .count(0)
                        .isReward(false)
                        .build();
        
                    rewards.add(reward);
                }
            }
        }

        // 보상 정보 갱신
        for(int i = 0; i < appears.size(); i++)
        {
            int count = exFun.getRandInt(appears.get(i).getMin(), appears.get(i).getMax());
            rewards.get(i).setAppearNum(i + 1);
            rewards.get(i).setCount(user.getClickCount() + count);
            rewards.get(i).setReward(false);
        }

        itemRewardJpaService.saveAll(rewards);
    }
}